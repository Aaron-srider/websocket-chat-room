package fit.wenchao.websocketchartroom.ws;

import fit.wenchao.websocketchartroom.dao.po.UserPO;
import fit.wenchao.websocketchartroom.dao.repo.UserDao;
import fit.wenchao.websocketchartroom.entity.UserVo;
import fit.wenchao.websocketchartroom.service.UserService;
import fit.wenchao.websocketchartroom.utils.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static fit.wenchao.utils.basic.BasicUtils.loop;
import static fit.wenchao.websocketchartroom.UserController.WrapperUtils.eq;
import static fit.wenchao.websocketchartroom.utils.HttpUtils.parseQueryString;
import static fit.wenchao.websocketchartroom.utils.JsonUtils.ofJson;
import static fit.wenchao.websocketchartroom.utils.ResultCodeEnum.SYS_USER_LEAVE_ROOM;
import static fit.wenchao.websocketchartroom.utils.WsServerUtils.returnMsg;
import static fit.wenchao.websocketchartroom.utils.WsServerUtils.returnMsgAndCloseSession;
import static java.util.Arrays.asList;

@ServerEndpoint("/chat")
@Component
@Slf4j
public class ChatEndPoint {

    /**
     * 保存用户的id和对应的ws连接
     */
    public static ConcurrentMap<Integer, ChatEndPoint> onlineUsers = new ConcurrentHashMap<>();

    Session session;

    UserPO currentUser;

    public static UserDao userDao;

    public static UserService userService;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @OnOpen
    public void onopen(Session session, EndpointConfig endpointConfig) throws Exception {

        //int i = 10/0;
        //检测请求是否有token
        String token = getToken(session);
        if (token == null) {
            returnMsgAndCloseSession(session, null, ResultCodeEnum.WS_TOKEN_MISSING, "error");
            return;
        }
        log.info("ws token: {}", token);

        //检查用户是否登录过，未登录则报错，登录过则放行
        UserPO userpo = userDao.getOne(eq("ws_token", token));
        if (userpo == null) {
            returnMsgAndCloseSession(session, null, ResultCodeEnum.USER_NOT_LOGIN_OR_EXISTS, "error");
            return;
        }
        log.info("user: {} has logged", userpo.getUsername());

        //检查用户是否已经打开了一个聊天窗口，如果打开，则不再建立socket连接
        if (userChatWindowOpened(onlineUsers, userpo.getId())) {
            returnMsgAndCloseSession(session, null, ResultCodeEnum.CHAT_WINDOW_OPENED, "error");
            return;
        }

        returnMsg(session, null, ResultCodeEnum.SUCCESS, "info");

        //用户没有打开聊天窗口，为其建立并保存socket连接
        this.currentUser = userpo;
        this.session = session;
        onlineUsers.put(userpo.getId(), this);

        UserVo userVo2Return2WsCient = UserVo.builder().id(userpo.getId())
                .username(userpo.getUsername()).build();

        //广播用户进入聊天室
        broadCast(onlineUsers, userVo2Return2WsCient,
                ResultCodeEnum.SYS_BROADCAST_USER_LOGIN,
                "info", null);

        broadCastAllOnlineUsers();

    }

    private void returnOnlineUserList(Session session, Map<String, ChatEndPoint> onlineUsers, String username) throws Exception {
        List<String> onlineUsernames = new ArrayList<>();
        loop(onlineUsers, (entry, key, value) -> {
            onlineUsernames.add(key);
        });
        returnMsg(session, onlineUsernames, ResultCodeEnum.RETURN_ONLINE_USERLIST, "info");
    }

    private void broadCast(Map<Integer, ChatEndPoint> onlineUsers, Object payload, ResultCodeEnum code,
                           String logLevel, Integer except) throws Exception {
        loop(onlineUsers, (entry, key, value) -> {
            if (!key.equals(except)) {
                returnMsg(
                        value.session,
                        payload,
                        code,
                        logLevel);
            }
        });
    }

    private boolean userChatWindowOpened(Map<Integer, ChatEndPoint> onlineUsers, Integer userid) {
        return onlineUsers.get(userid) != null;
    }


    private String getToken(Session session) {
        String queryString = session.getQueryString();
        if (queryString == null) {
            return null;
        }

        Map<String, String> stringObjectMap = parseQueryString(queryString);

        String token = stringObjectMap.get("token");
        return token;
    }


    @OnMessage
    public void onMessage(Session session, String message) {

    }

    private void broadCastAllOnlineUsers() throws Exception {
        List<Integer> userPOContainId = onlineUsers.keySet().stream()
                .collect(Collectors.toList());


        if (userPOContainId.size() != 0) {
            List<UserPO> userPOListContainIdAndUsername = userDao.listByIds(userPOContainId);


            List<UserVo> userVoListContainIdAndUsername = userPOListContainIdAndUsername.stream()
                    .map(userpo2trans -> UserVo.builder()
                            .id(userpo2trans.getId())
                            .username(userpo2trans.getUsername()).build())
                    .collect(Collectors.toList());
            //广播所有用户列表
            broadCast(onlineUsers,
                    ofJson(
                            "onlineUsers", userVoListContainIdAndUsername
                    ),
                    ResultCodeEnum.SYS_BROADCAST_ONLINE_USERLIST
                    , "info", null);

        } else {
            broadCast(onlineUsers,
                    ofJson(
                            "onlineUsers", new ArrayList<>()
                    ),
                    ResultCodeEnum.SYS_BROADCAST_ONLINE_USERLIST
                    , "info", null);
        }
    }

    @OnClose
    public void onClose(Session session) {
        log.info("session: {} leave", session.getId());
        userLeave();
    }

    private void userLeave() {
        try {
            userLeaveChatRoom(currentUser.getId(), onlineUsers);

            //通知聊天室的所有用户当前用户离开
            broadCastUserLeaveChatRoom();

            broadCastAllOnlineUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void userLeaveChatRoom(Integer userid, ConcurrentMap<Integer, ChatEndPoint> onlineUsers) throws IOException {
        if (onlineUsers.get(userid) != null) {
            onlineUsers.get(userid).getSession().close();
            onlineUsers.remove(userid);
        }
    }

    private void broadCastUserLeaveChatRoom() throws Exception {
        //通知聊天室的所有用户当前用户离开
        broadCast(onlineUsers,
                UserVo.builder().id(currentUser.getId()).username(currentUser.getUsername()).build(),
                SYS_USER_LEAVE_ROOM,
                "info",
                currentUser.getId()
        );
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("session: {} leave，throwable:{}", session.getId(), throwable);
        userLeave();
    }

    public Session getSession() {
        return session;
    }
}

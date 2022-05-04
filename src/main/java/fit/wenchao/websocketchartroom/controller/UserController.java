package fit.wenchao.websocketchartroom.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import fit.wenchao.websocketchartroom.BackendException;
import fit.wenchao.websocketchartroom.JsonResult;
import fit.wenchao.websocketchartroom.dao.po.UserPO;
import fit.wenchao.websocketchartroom.dao.repo.UserDao;
import fit.wenchao.websocketchartroom.entity.UserVo;
import fit.wenchao.websocketchartroom.utils.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

import static fit.wenchao.websocketchartroom.controller.UserController.WrapperUtils.eq;
import static fit.wenchao.websocketchartroom.utils.JsonUtils.ofJson;
import static fit.wenchao.websocketchartroom.utils.ResultCodeEnum.USER_LOGGED;
import static fit.wenchao.websocketchartroom.utils.WsServerUtils.returnMsg;
import static fit.wenchao.websocketchartroom.ws.ChatEndPoint.onlineUsers;
import static fit.wenchao.websocketchartroom.ws.ChatEndPoint.userLeaveChatRoom;

@RestController
@Slf4j
@CrossOrigin(allowCredentials = "true")
public class UserController {

    @Autowired
    UserDao userDao;

    @PutMapping("/logout")
    public JsonResult logout(@RequestBody UserVo userVo) throws IOException {
        UserPO username = userDao.getOne(eq("username", userVo.getUsername()));

        if (username == null) {
            return null;
        }

        returnMsg(onlineUsers.get(username.getId()).getSession(),
                null,
                ResultCodeEnum.USER_LOGOUT,
                "info"
        );

        userLeaveChatRoom(username.getId(), onlineUsers);

        userDao.update(
                null,
                Wrappers.<UserPO>lambdaUpdate()
                        .eq(UserPO::getId, username.getId())
                        .set(UserPO::getWsToken, null));

        return JsonResult.ok(null);
    }

    public static class WrapperUtils {
        public static <T> QueryWrapper<T> eq(String column, Object value) {
            return new QueryWrapper<T>().eq(column, value);
        }
    }

    @PostMapping("/login")
    public JsonResult login(@RequestBody UserVo userVo) throws IOException {
        String username = userVo.getUsername();

        //检查用户名密码是否正确
        UserPO userPO = checkUsernameAndPassword(userVo);

        //检查用户是否已经登录，是则强迫已经登录的用户下线
        checkUserLoggedOrNot(userPO);

        //为用户设置新的token，用于ws服务器识别用户
        userPO.setWsToken(UUID.randomUUID().toString());
        userDao.updateById(userPO);

        return JsonResult.ok(
                UserVo.builder().id(userPO.getId())
                        .username(username)
                        .token(userPO.getWsToken()).build()

        );

    }

    private void checkUserLoggedOrNot(UserPO userPO) throws IOException {
        String wsToken = userPO.getWsToken();
        if (wsToken != null) {
            synchronized (this) {
                if (onlineUsers.get(userPO.getId()) != null) {
                    returnMsg(onlineUsers.get(userPO.getId()).getSession(),
                            null,
                            USER_LOGGED,
                            "error");
                    userDao.update(null,
                            Wrappers.
                                    <UserPO>lambdaUpdate()
                                    .eq(UserPO::getId, userPO.getId())
                                    .set(UserPO::getWsToken, null));
                }
            }

        }
    }

    private UserPO checkUsernameAndPassword(UserVo userVo) {
        UserPO username1 = userDao.getOne(eq("username", userVo.getUsername()));
        if (username1 == null) {
            log.error(ResultCodeEnum.USER_UNREGISTERED.getMessage());
            throw new BackendException(
                    ofJson(
                            "username", userVo.getUsername()
                    ),
                    ResultCodeEnum.USER_UNREGISTERED,
                    null);
        }

        if (!userVo.getPassword().equals(username1.getPassword())) {
            log.error(ResultCodeEnum.PWD_ERROR.getMessage());
            throw new BackendException(
                    null,
                    ResultCodeEnum.PWD_ERROR,
                    null);
        }
        return username1;
    }

}

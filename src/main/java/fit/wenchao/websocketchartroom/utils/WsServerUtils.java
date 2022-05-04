package fit.wenchao.websocketchartroom.utils;

import com.alibaba.fastjson.JSONObject;
import fit.wenchao.websocketchartroom.JsonResult;

import javax.websocket.Session;
import java.io.IOException;

import static fit.wenchao.websocketchartroom.utils.LogUtils.log;

public class WsServerUtils {
    /**
     * 向指定的ws客户端返回消息，消息格式为{@code JsonResult}
     * @param session 客户端session
     * @param  data 返回的载荷数据
     * @param resultCodeEnum 返回的code和message
     * @param logLevel 打印日志级别
     */
    public static void returnMsg(Session session, Object data, ResultCodeEnum resultCodeEnum,
                           String logLevel) throws IOException {
        log(logLevel, resultCodeEnum.getMessage());
        JsonResult instance = JsonResult.getInstance(data, resultCodeEnum, null);
        session.getBasicRemote().sendText(JSONObject.toJSONString(instance));
    }

    /**
     * 向指定的ws客户端返回消息，消息格式为{@code JsonResult}，返回消息后关闭该客户端在服务器端的ws连接
     * @param session 客户端session
     * @param  data 返回的载荷数据
     * @param resultCodeEnum 返回的code和message
     * @param logLevel 打印日志级别
     */
    public static void returnMsgAndCloseSession(Session session, Object data,  ResultCodeEnum resultCodeEnum, String logLevel) throws IOException {
        log(logLevel, resultCodeEnum.getMessage());
        JsonResult instance = JsonResult.getInstance(data, resultCodeEnum, null);
        session.getBasicRemote().sendText(JSONObject.toJSONString(instance));
        session.close();
    }
}

package fit.wenchao.websocketchartroom.utils;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {

    TOKEN_EXPIRED(100, "token已失效"),
    USER_INFO_NOT_COMPLETE(101, "用户信息未完善"),
    FRONT_DATA_MISSING(102, "前端数据缺失"),

    OAUTH_CODE_INVALID(104, "无效的 oauth_code"),
    OAUTH_CODE_BEEN_USED(105, "oauth_code 已被使用"),
    USER_INFO_COMPLETED(106, "用户信息无需完善"),
    ATTACHMENT_NOT_EXISTS(107, "附件不存在"),
    FRONT_DATA_REDUNDANT(108, "前端数据冗余"),

    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(202, "服务异常"),
    DATA_ERROR(204, "数据异常"),

    SIGN_ERROR(300, "签名错误"),
    SIGN_MISSING(301, "签名丢失"),

    PAY_PASSWORD_ERROR(401, "支付密码错误"),
    REPEAT_ERROR(402, "重复提交"),


    UNKNOWN_ERROR(500, "未知后端异常"),
    USER_UNREGISTERED(502, "此用户未注册"),
    WS_TOKEN_MISSING(503, "ws token 缺失"),
    USER_NOT_LOGIN_OR_EXISTS(501, "用户未登录或用户不存在"),
    SYS_BROADCAST_ONLINE_USERLIST(700, "广播在线用户列表"),
    SYS_BROADCAST_USER_LOGIN(7002, "广播用户进入聊天室"),
    RETURN_ONLINE_USERLIST(7001, "用户登录ws服务器时，返回当前在线的用户列表"),
    CHAT_WINDOW_OPENED(5001, "用户已经打开了一个聊天窗口，无法再打开一个"),
    USER_LOGGED(5003, "用户已经登录"),
    PWD_ERROR(5002, "密码错误"), USER_LOGOUT(5004, "用户登出，退出聊天室"),
    SYS_USER_LEAVE_ROOM(5005, "用户离开聊天室");

    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

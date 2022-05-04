package fit.wenchao.websocketchartroom.service.impl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class UserDTO {

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * websocket sessionid
     */
    private String wsSessionId;

    /**
     * 每次登录时生成一个随机的uuid，用于ws服务器识别用户
     */
    private String wsToken;
}

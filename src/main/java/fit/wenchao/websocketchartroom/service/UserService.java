package fit.wenchao.websocketchartroom.service;

import fit.wenchao.websocketchartroom.service.impl.UserDTO;

/**
 * <p>
 * User 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-30
 */
public interface UserService {
    UserDTO getUserByUsername(String username);
}



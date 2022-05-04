package fit.wenchao.websocketchartroom.service.impl;

import fit.wenchao.websocketchartroom.dao.po.UserPO;
import fit.wenchao.websocketchartroom.dao.repo.UserDao;
import fit.wenchao.websocketchartroom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static fit.wenchao.websocketchartroom.controller.UserController.WrapperUtils.eq;

/**
 * <p>
 * User 服务类
 * </p>
 *
 * @author wc
 * @since 2022-04-30
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    public UserDTO getUserByUsername(String username) {
        UserPO username1 = userDao.getOne(eq("username", username));
        UserDTO userDTO = UserDTO.builder().username(username).id(username1.getId()).build();
        return userDTO;
    }


}



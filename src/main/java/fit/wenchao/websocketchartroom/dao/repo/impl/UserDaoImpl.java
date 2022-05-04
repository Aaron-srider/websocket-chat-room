package fit.wenchao.websocketchartroom.dao.repo.impl;

import fit.wenchao.websocketchartroom.dao.po.UserPO;
import fit.wenchao.websocketchartroom.dao.mapper.UserMapper;
import fit.wenchao.websocketchartroom.dao.repo.UserDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Repository;

/**
 * <p>
 *  dao实现类
 * </p>
 *
 * @author wc
 * @since 2022-04-30
 */
@Repository
public class UserDaoImpl extends ServiceImpl<UserMapper, UserPO> implements UserDao {

}

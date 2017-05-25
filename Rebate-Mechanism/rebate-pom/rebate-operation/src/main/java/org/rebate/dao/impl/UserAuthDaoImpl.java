package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.UserAuth;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.UserAuthDao;
@Repository("userAuthDaoImpl")
public class UserAuthDaoImpl extends  BaseDaoImpl<UserAuth,Long> implements UserAuthDao {

}
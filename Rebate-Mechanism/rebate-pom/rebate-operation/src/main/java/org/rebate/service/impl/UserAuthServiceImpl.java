package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.UserAuth;
import org.rebate.dao.UserAuthDao;
import org.rebate.service.UserAuthService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("userAuthServiceImpl")
public class UserAuthServiceImpl extends BaseServiceImpl<UserAuth,Long> implements UserAuthService {

      @Resource(name="userAuthDaoImpl")
      public void setBaseDao(UserAuthDao userAuthDao) {
         super.setBaseDao(userAuthDao);
  }
}
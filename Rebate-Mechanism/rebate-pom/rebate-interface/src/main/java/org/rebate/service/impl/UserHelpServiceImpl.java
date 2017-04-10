package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.UserHelp;
import org.rebate.dao.UserHelpDao;
import org.rebate.service.UserHelpService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("userHelpServiceImpl")
public class UserHelpServiceImpl extends BaseServiceImpl<UserHelp,Long> implements UserHelpService {

      @Resource(name="userHelpDaoImpl")
      public void setBaseDao(UserHelpDao userHelpDao) {
         super.setBaseDao(userHelpDao);
  }
}
package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.MsgEndUser;
import org.rebate.dao.MsgEndUserDao;
import org.rebate.service.MsgEndUserService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("msgEndUserServiceImpl")
public class MsgEndUserServiceImpl extends BaseServiceImpl<MsgEndUser,Long> implements MsgEndUserService {

      @Resource(name="msgEndUserDaoImpl")
      public void setBaseDao(MsgEndUserDao msgEndUserDao) {
         super.setBaseDao(msgEndUserDao);
  }
}
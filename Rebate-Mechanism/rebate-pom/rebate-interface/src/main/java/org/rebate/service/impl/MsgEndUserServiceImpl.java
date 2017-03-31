package org.rebate.service.impl; 

import javax.annotation.Resource;

import org.rebate.dao.MsgEndUserDao;
import org.rebate.entity.EndUser;
import org.rebate.entity.MessageInfo;
import org.rebate.entity.MsgEndUser;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.MsgEndUserService;
import org.springframework.stereotype.Service;

@Service("msgEndUserServiceImpl")
public class MsgEndUserServiceImpl extends BaseServiceImpl<MsgEndUser,Long> implements MsgEndUserService {

	 @Resource(name = "msgEndUserDaoImpl")
	  private MsgEndUserDao msgEndUserDao;

      @Resource(name="msgEndUserDaoImpl")
      public void setBaseDao(MsgEndUserDao msgEndUserDao) {
         super.setBaseDao(msgEndUserDao);
      }

      @Override
      public MsgEndUser findMsgEndUserByUserAndMsg(EndUser endUser, MessageInfo msg) {
        return msgEndUserDao.findMsgEndUserByUserAndMsg(endUser, msg);
      }
      
      
}
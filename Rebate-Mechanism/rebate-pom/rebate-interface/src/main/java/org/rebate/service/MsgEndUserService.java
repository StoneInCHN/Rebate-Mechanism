package org.rebate.service; 

import org.rebate.entity.EndUser;
import org.rebate.entity.MessageInfo;
import org.rebate.entity.MsgEndUser;
import org.rebate.framework.service.BaseService;

public interface MsgEndUserService extends BaseService<MsgEndUser,Long>{

  MsgEndUser findMsgEndUserByUserAndMsg(EndUser endUser, MessageInfo msg);

}
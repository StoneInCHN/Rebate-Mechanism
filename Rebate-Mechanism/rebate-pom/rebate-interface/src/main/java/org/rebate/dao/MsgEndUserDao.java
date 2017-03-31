package org.rebate.dao; 
import org.rebate.entity.EndUser;
import org.rebate.entity.MessageInfo;
import org.rebate.entity.MsgEndUser;
import org.rebate.framework.dao.BaseDao;

public interface MsgEndUserDao extends  BaseDao<MsgEndUser,Long>{

  MsgEndUser findMsgEndUserByUserAndMsg(EndUser endUser, MessageInfo msg);

}
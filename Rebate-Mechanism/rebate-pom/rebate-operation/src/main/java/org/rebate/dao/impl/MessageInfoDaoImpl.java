package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.MessageInfo;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.MessageInfoDao;
@Repository("messageInfoDaoImpl")
public class MessageInfoDaoImpl extends  BaseDaoImpl<MessageInfo,Long> implements MessageInfoDao {

}
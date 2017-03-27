package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.MessageInfo;
import org.rebate.dao.MessageInfoDao;
import org.rebate.service.MessageInfoService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("messageInfoServiceImpl")
public class MessageInfoServiceImpl extends BaseServiceImpl<MessageInfo,Long> implements MessageInfoService {

      @Resource(name="messageInfoDaoImpl")
      public void setBaseDao(MessageInfoDao messageInfoDao) {
         super.setBaseDao(messageInfoDao);
  }
}
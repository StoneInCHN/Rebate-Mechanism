package org.rebate.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.rebate.dao.MessageInfoDao;
import org.rebate.entity.EndUser;
import org.rebate.entity.MessageInfo;
import org.rebate.entity.MsgEndUser;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.EndUserService;
import org.rebate.service.MessageInfoService;
import org.rebate.service.MsgEndUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("messageInfoServiceImpl")
public class MessageInfoServiceImpl extends BaseServiceImpl<MessageInfo, Long> implements
    MessageInfoService {

  @Resource(name="endUserServiceImpl")
  private EndUserService endUserService;
  
  @Resource(name="msgEndUserServiceImpl")
  private MsgEndUserService msgEndUserService;
  
  @Resource(name = "messageInfoDaoImpl")
  public void setBaseDao(MessageInfoDao messageInfoDao) {
    super.setBaseDao(messageInfoDao);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void save(MessageInfo entity) {
    super.save(entity);
    List<EndUser> endUsers = endUserService.findAll();
    if(endUsers!=null && endUsers.size() >0){
      for (EndUser endUser : endUsers) {
        MsgEndUser msg = new MsgEndUser();
        msg.setEndUser(endUser);
        msg.setMessage(entity);
        msg.setIsRead(false);
        msg.setIsPush(false);
        msgEndUserService.save(msg);
      }
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void delete(Long... ids) {
    if(ids!=null && ids.length>0){
      for (Long id : ids) {
        MessageInfo messageInfo = super.find(id);
         Set<MsgEndUser> msgs= messageInfo.getMsgUser();
         if(msgs!=null&& msgs.size() >0){
           for (MsgEndUser msgEndUser : msgs) {
             msgEndUserService.delete(msgEndUser);
          }
         }
         super.delete(messageInfo);
      }
    }
  }

  
}

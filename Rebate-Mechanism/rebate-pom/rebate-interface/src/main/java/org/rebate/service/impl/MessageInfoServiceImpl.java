package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.EndUserDao;
import org.rebate.dao.MessageInfoDao;
import org.rebate.dao.MsgEndUserDao;
import org.rebate.entity.MessageInfo;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.MessageInfoService;
import org.springframework.stereotype.Service;

@Service("messageInfoServiceImpl")
public class MessageInfoServiceImpl extends BaseServiceImpl<MessageInfo, Long> implements
    MessageInfoService {

  @Resource(name = "messageInfoDaoImpl")
  private MessageInfoDao messageInfoDao;

  @Resource(name = "endUserDaoImpl")
  private EndUserDao endUserDao;

  @Resource(name = "msgEndUserDaoImpl")
  private MsgEndUserDao msgEndUserDao;


  @Resource(name = "messageInfoDaoImpl")
  public void setBaseDao(MessageInfoDao messageInfoDao) {
    super.setBaseDao(messageInfoDao);
  }

  @Override
  public Page<MessageInfo> findMsgByUser(Long userId, Pageable pageable) {
    return messageInfoDao.findMsgByUser(userId, pageable);
  }

}

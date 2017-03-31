package org.rebate.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rebate.dao.MessageInfoDao;
import org.rebate.entity.MessageInfo;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository("messageInfoDaoImpl")
public class MessageInfoDaoImpl extends BaseDaoImpl<MessageInfo, Long> implements MessageInfoDao {

  @Override
  public Page<MessageInfo> findMsgByUser(Long userId, Pageable pageable) {
    if (userId == null) {
      return null;
    }
    try {
      String jpql =
          "select msg from MessageInfo AS msg inner join msg.msgUser as mm WHERE mm.endUser.id = :userId order by msg.createDate desc";
      Map<String, Object> paramMap = new HashMap<String, Object>();
      paramMap.put("userId", userId);
      return findPageCustomized(pageable, jpql, paramMap);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public List<MessageInfo> findMsgByUser(Long userId) {
    if (userId == null) {
      return null;
    }
    try {
      String jpql =
          "select msg from MessageInfo AS msg inner join msg.msgUser as mm WHERE mm.endUser.id = :userId order by msg.createDate desc";
      Map<String, Object> paramMap = new HashMap<String, Object>();
      paramMap.put("userId", userId);
      return findListCustomized(jpql, paramMap);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}

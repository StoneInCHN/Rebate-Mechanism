package org.rebate.dao.impl;


import javax.persistence.FlushModeType;

import org.rebate.dao.MsgEndUserDao;
import org.rebate.entity.EndUser;
import org.rebate.entity.MessageInfo;
import org.rebate.entity.MsgEndUser;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("msgEndUserDaoImpl")
public class MsgEndUserDaoImpl extends BaseDaoImpl<MsgEndUser, Long> implements MsgEndUserDao {

  @Override
  public MsgEndUser findMsgEndUserByUserAndMsg(EndUser endUser, MessageInfo msg) {
    if (endUser == null || msg == null) {
      return null;
    }
    try {
      String jpql =
          "select meu from MsgEndUser AS meu where meu.endUser=:endUser and meu.message=:message";
      return entityManager.createQuery(jpql, MsgEndUser.class).setFlushMode(FlushModeType.COMMIT)
          .setParameter("endUser", endUser).setParameter("message", msg).getSingleResult();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}

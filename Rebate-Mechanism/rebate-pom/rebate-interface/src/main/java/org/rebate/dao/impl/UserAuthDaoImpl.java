package org.rebate.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.UserAuthDao;
import org.rebate.entity.UserAuth;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("userAuthDaoImpl")
public class UserAuthDaoImpl extends BaseDaoImpl<UserAuth, Long> implements UserAuthDao {


  @Override
  public UserAuth getUserAuth(Long userId, Boolean isAuth) {
    if (userId == null) {
      return null;
    }
    try {
      String jpql =
          "select auth from UserAuth auth where auth.userId = :userId and auth.isAuth = :isAuth";
      return entityManager.createQuery(jpql, UserAuth.class).setFlushMode(FlushModeType.COMMIT)
          .setParameter("userId", userId).setParameter("isAuth", isAuth).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Override
  public UserAuth getUserAuthByIdCard(String cardNo, Boolean isAuth) {

    if (cardNo == null) {
      return null;
    }
    try {
      String jpql =
          "select auth from UserAuth auth where auth.idCardNo = :cardNo and auth.isAuth = :isAuth";
      return entityManager.createQuery(jpql, UserAuth.class).setFlushMode(FlushModeType.COMMIT)
          .setParameter("cardNo", cardNo).setParameter("isAuth", isAuth).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }

  }


}

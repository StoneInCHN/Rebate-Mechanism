package org.rebate.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.UserRecommendRelationDao;
import org.rebate.entity.EndUser;
import org.rebate.entity.UserRecommendRelation;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("userRecommendRelationDaoImpl")
public class UserRecommendRelationDaoImpl extends BaseDaoImpl<UserRecommendRelation, Long>
    implements UserRecommendRelationDao {

  @Override
  public UserRecommendRelation findByUser(EndUser endUser) {
    if (endUser == null) {
      return null;
    }
    try {
      String jpql =
          "select relation from UserRecommendRelation relation where relation.endUser = :endUser";
      return entityManager.createQuery(jpql, UserRecommendRelation.class)
          .setFlushMode(FlushModeType.COMMIT).setParameter("endUser", endUser).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}

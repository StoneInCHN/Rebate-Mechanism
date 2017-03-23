package org.rebate.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.UserRecommendRelationDao;
import org.rebate.entity.UserRecommendRelation;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("userRecommendRelationDaoImpl")
public class UserRecommendRelationDaoImpl extends BaseDaoImpl<UserRecommendRelation, Long>
    implements UserRecommendRelationDao {

  @Override
  public UserRecommendRelation findByUserMobile(String mobileNo) {
    if (mobileNo == null) {
      return null;
    }
    try {
      String jpql =
          "select relation from UserRecommendRelation relation where relation.cellPhoneNum = :cellPhoneNum";
      return entityManager.createQuery(jpql, UserRecommendRelation.class)
          .setFlushMode(FlushModeType.COMMIT).setParameter("cellPhoneNum", mobileNo)
          .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}

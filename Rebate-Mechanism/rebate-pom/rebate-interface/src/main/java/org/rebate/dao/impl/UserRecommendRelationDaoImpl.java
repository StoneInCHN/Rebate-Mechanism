package org.rebate.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.rebate.dao.UserRecommendRelationDao;
import org.rebate.entity.EndUser;
import org.rebate.entity.UserRecommendRelation;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
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

  @Override
  public Page<UserRecommendRelation> getRelationsByRecommender(Long userId, Pageable pageable) {
    if (userId == null) {
      return null;
    }
    try {
      String jpql =
          "select relation from UserRecommendRelation as relation inner join relation.parent as rparent where rparent.endUser.id =:userId order by relation.createDate desc";
      Map<String, Object> paramMap = new HashMap<String, Object>();
      paramMap.put("userId", userId);
      return findPageCustomized(pageable, jpql, paramMap);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}

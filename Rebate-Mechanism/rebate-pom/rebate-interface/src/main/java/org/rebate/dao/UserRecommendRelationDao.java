package org.rebate.dao;

import org.rebate.entity.EndUser;
import org.rebate.entity.UserRecommendRelation;
import org.rebate.framework.dao.BaseDao;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;

public interface UserRecommendRelationDao extends BaseDao<UserRecommendRelation, Long> {

  /**
   * 根据用户查找终端用户推荐关系
   * 
   * @param EndUser
   * @return 终端用户， 若不存在则返回null
   */
  UserRecommendRelation findByUser(EndUser endUser);

  /**
   * 查找用户的推荐记录
   * 
   * @param userId
   * @param pageable
   * @return
   */
  Page<UserRecommendRelation> getRelationsByRecommender(Long userId, Pageable pageable);
}

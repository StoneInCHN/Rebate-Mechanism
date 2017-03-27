package org.rebate.service;

import org.rebate.entity.EndUser;
import org.rebate.entity.UserRecommendRelation;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.rebate.framework.service.BaseService;

public interface UserRecommendRelationService extends BaseService<UserRecommendRelation, Long> {

  /**
   * 根据手机号码查找终端用户推荐关系
   * 
   * @param endUser 用户
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

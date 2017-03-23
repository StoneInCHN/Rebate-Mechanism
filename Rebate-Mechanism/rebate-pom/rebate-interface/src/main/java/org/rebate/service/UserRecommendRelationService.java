package org.rebate.service;

import org.rebate.entity.UserRecommendRelation;
import org.rebate.framework.service.BaseService;

public interface UserRecommendRelationService extends BaseService<UserRecommendRelation, Long> {

  /**
   * 根据手机号码查找终端用户推荐关系
   * 
   * @param mobileNo 手机号
   * @return 终端用户， 若不存在则返回null
   */
  UserRecommendRelation findByUserMobile(String mobileNo);
}

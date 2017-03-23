package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.UserRecommendRelationDao;
import org.rebate.entity.UserRecommendRelation;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.UserRecommendRelationService;
import org.springframework.stereotype.Service;

@Service("userRecommendRelationServiceImpl")
public class UserRecommendRelationServiceImpl extends BaseServiceImpl<UserRecommendRelation, Long>
    implements UserRecommendRelationService {

  @Resource(name = "userRecommendRelationDaoImpl")
  private UserRecommendRelationDao userRecommendRelationDao;

  @Resource(name = "userRecommendRelationDaoImpl")
  public void setBaseDao(UserRecommendRelationDao userRecommendRelationDao) {
    super.setBaseDao(userRecommendRelationDao);
  }

  @Override
  public UserRecommendRelation findByUserMobile(String mobileNo) {
    // TODO Auto-generated method stub
    return null;
  }
}

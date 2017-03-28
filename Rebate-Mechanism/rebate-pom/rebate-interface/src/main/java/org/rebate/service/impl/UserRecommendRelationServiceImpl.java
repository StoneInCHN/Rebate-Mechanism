package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.UserRecommendRelationDao;
import org.rebate.entity.EndUser;
import org.rebate.entity.UserRecommendRelation;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
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
  public UserRecommendRelation findByUser(EndUser endUser) {
    return userRecommendRelationDao.findByUser(endUser);
  }

  @Override
  public Page<UserRecommendRelation> getRelationsByRecommender(Long userId, Pageable pageable) {
    return userRecommendRelationDao.getRelationsByRecommender(userId, pageable);
  }
}

package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.UserRecommendRelation;
import org.rebate.dao.UserRecommendRelationDao;
import org.rebate.service.UserRecommendRelationService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("userRecommendRelationServiceImpl")
public class UserRecommendRelationServiceImpl extends BaseServiceImpl<UserRecommendRelation,Long> implements UserRecommendRelationService {

      @Resource(name="userRecommendRelationDaoImpl")
      public void setBaseDao(UserRecommendRelationDao userRecommendRelationDao) {
         super.setBaseDao(userRecommendRelationDao);
  }
}
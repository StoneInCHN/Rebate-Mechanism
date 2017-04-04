package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.UserRecommendRelation;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.UserRecommendRelationDao;
@Repository("userRecommendRelationDaoImpl")
public class UserRecommendRelationDaoImpl extends  BaseDaoImpl<UserRecommendRelation,Long> implements UserRecommendRelationDao {

}
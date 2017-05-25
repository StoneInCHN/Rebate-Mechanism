package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.ClearingOrderRelation;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.ClearingOrderRelationDao;
@Repository("clearingOrderRelationDaoImpl")
public class ClearingOrderRelationDaoImpl extends  BaseDaoImpl<ClearingOrderRelation,Long> implements ClearingOrderRelationDao {

}
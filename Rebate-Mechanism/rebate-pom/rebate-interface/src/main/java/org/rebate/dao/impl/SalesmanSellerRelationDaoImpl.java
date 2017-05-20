package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.SalesmanSellerRelation;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.SalesmanSellerRelationDao;
@Repository("salesmanSellerRelationDaoImpl")
public class SalesmanSellerRelationDaoImpl extends  BaseDaoImpl<SalesmanSellerRelation,Long> implements SalesmanSellerRelationDao {

}
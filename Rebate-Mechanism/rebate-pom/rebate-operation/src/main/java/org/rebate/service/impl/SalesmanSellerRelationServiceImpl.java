package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.SalesmanSellerRelation;
import org.rebate.dao.SalesmanSellerRelationDao;
import org.rebate.service.SalesmanSellerRelationService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("salesmanSellerRelationServiceImpl")
public class SalesmanSellerRelationServiceImpl extends BaseServiceImpl<SalesmanSellerRelation,Long> implements SalesmanSellerRelationService {

      @Resource(name="salesmanSellerRelationDaoImpl")
      public void setBaseDao(SalesmanSellerRelationDao salesmanSellerRelationDao) {
         super.setBaseDao(salesmanSellerRelationDao);
  }
}
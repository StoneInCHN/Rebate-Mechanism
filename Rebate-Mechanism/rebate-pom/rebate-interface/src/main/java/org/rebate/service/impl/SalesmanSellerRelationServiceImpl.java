package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.SalesmanSellerRelationDao;
import org.rebate.entity.SalesmanSellerRelation;
import org.rebate.entity.Seller;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.SalesmanSellerRelationService;
import org.springframework.stereotype.Service;

@Service("salesmanSellerRelationServiceImpl")
public class SalesmanSellerRelationServiceImpl extends
    BaseServiceImpl<SalesmanSellerRelation, Long> implements SalesmanSellerRelationService {

  @Resource(name = "salesmanSellerRelationDaoImpl")
  private SalesmanSellerRelationDao salesmanSellerRelationDao;

  @Resource(name = "salesmanSellerRelationDaoImpl")
  public void setBaseDao(SalesmanSellerRelationDao salesmanSellerRelationDao) {
    super.setBaseDao(salesmanSellerRelationDao);
  }


  @Override
  public SalesmanSellerRelation getRelationBySeller(Seller seller) {
    return salesmanSellerRelationDao.getRelationBySeller(seller);
  }
}

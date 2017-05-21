package org.rebate.dao;

import org.rebate.entity.SalesmanSellerRelation;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerApplication;
import org.rebate.framework.dao.BaseDao;

public interface SalesmanSellerRelationDao extends BaseDao<SalesmanSellerRelation, Long> {
  /**
   * 根据商家获取业务员推荐商家关系
   * 
   * @param seller
   * @return
   */
  SalesmanSellerRelation getRelationBySeller(Seller seller);


  /**
   * 根据商家申请记录获取业务员推荐商家关系
   * 
   * @param seller
   * @return
   */
  SalesmanSellerRelation getRelationBySellerApplication(SellerApplication sellerApplication);
}

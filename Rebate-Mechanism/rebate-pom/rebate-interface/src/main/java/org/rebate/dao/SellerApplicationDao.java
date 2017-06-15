package org.rebate.dao;

import org.rebate.entity.SellerApplication;
import org.rebate.framework.dao.BaseDao;

public interface SellerApplicationDao extends BaseDao<SellerApplication, Long> {
  /**
   * 根据营业执照号获取商户申请信息
   * 
   * @param license
   * @return
   */
  SellerApplication findSellerApplicationBylicense(String license);
}

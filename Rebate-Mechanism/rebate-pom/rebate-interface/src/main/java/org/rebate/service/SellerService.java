package org.rebate.service;

import org.rebate.entity.Seller;
import org.rebate.framework.service.BaseService;

public interface SellerService extends BaseService<Seller, Long> {

  /**
   * 根据用户获取商户信息
   * 
   * @param userId
   * @return
   */
  Seller findSellerByUser(Long userId);
}

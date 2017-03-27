package org.rebate.service;

import org.rebate.entity.Seller;
import org.rebate.framework.service.BaseService;
import org.rebate.json.request.SellerRequest;

public interface SellerService extends BaseService<Seller, Long> {

  /**
   * 根据用户获取商户信息
   * 
   * @param userId
   * @return
   */
  Seller findSellerByUser(Long userId);

  /**
   * 修改商户信息
   * 
   * @param req
   * @return
   */
  Seller editInfo(SellerRequest req);

}

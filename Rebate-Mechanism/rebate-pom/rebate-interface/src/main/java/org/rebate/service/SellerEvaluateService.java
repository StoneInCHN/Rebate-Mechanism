package org.rebate.service;

import org.rebate.entity.SellerEvaluate;
import org.rebate.framework.service.BaseService;

public interface SellerEvaluateService extends BaseService<SellerEvaluate, Long> {

  /**
   * 根据订单获取评价
   * 
   * @param orderId
   * @return
   */
  SellerEvaluate getEvaluateByOrder(Long orderId);
}

package org.rebate.service;

import java.math.BigDecimal;

import org.rebate.entity.Order;
import org.rebate.framework.service.BaseService;


public interface OrderService extends BaseService<Order, Long> {

  /**
   * 支付订单
   * 
   * @param userId
   * @param payType
   * @param amount
   * @param sellerId
   * @param remark
   * @return
   */
  Order create(Long userId, String payType, BigDecimal amount, Long sellerId, String remark,
      Boolean isBeanPay);
}

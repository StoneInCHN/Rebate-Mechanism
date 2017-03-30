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


  /**
   * 支付成功回调更新订单数据
   * 
   * @param orderSn
   * @return
   */
  Order updateOrderforPayCallBack(String orderSn);


  /**
   * 根据订单号获取订单
   * 
   * @param orderSn
   * @return
   */
  Order getOrderBySn(String orderSn);
}

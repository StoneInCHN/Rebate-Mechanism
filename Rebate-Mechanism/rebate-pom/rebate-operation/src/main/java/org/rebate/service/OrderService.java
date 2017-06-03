package org.rebate.service;

import org.rebate.entity.Order;
import org.rebate.entity.commonenum.CommonEnum.OrderStatus;
import org.rebate.framework.service.BaseService;

public interface OrderService extends BaseService<Order, Long> {

  /**
   * 手动更新录单订单支付状态及相关信息
   * 
   * @param orderId
   * @return
   */
  Order updateSallerOrderStatus(Long orderId, OrderStatus status);
}

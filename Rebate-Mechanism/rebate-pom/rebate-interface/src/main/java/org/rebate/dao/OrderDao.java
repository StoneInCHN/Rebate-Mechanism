package org.rebate.dao;

import java.util.List;

import org.rebate.entity.Order;
import org.rebate.framework.dao.BaseDao;

public interface OrderDao extends BaseDao<Order, Long> {


  /**
   * 根据订单号获取订单
   * 
   * @param orderSn
   * @return
   */
  Order getOrderBySn(String orderSn);

  /**
   * 根据批量订单流水号获取订单
   * 
   * @param orderSn
   * @return
   */
  List<Order> getOrderByBatchSn(String batchSn);

}

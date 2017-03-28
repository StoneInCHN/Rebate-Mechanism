package org.rebate.dao;

import org.rebate.entity.SellerEvaluate;
import org.rebate.framework.dao.BaseDao;

public interface SellerEvaluateDao extends BaseDao<SellerEvaluate, Long> {
  /**
   * 根据订单获取评价
   * 
   * @param orderId
   * @return
   */
  SellerEvaluate getEvaluateByOrder(Long orderId);
}

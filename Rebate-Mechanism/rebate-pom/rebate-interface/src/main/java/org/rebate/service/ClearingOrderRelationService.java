package org.rebate.service;

import org.rebate.entity.ClearingOrderRelation;
import org.rebate.framework.paging.Page;
import org.rebate.framework.service.BaseService;

public interface ClearingOrderRelationService extends BaseService<ClearingOrderRelation, Long> {

  /**
   * 根据提现单号获取对应的订单记录
   * 
   * @return
   */
  Page<ClearingOrderRelation> getOrdersByClearingId(Long clearingId, Integer pageSize,
      Integer pageNum);
}

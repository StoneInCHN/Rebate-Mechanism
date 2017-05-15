package org.rebate.service;

import org.rebate.entity.SellerEvaluate;
import org.rebate.entity.commonenum.CommonEnum.CommonStatus;
import org.rebate.framework.service.BaseService;

public interface SellerEvaluateService extends BaseService<SellerEvaluate, Long> {


  /**
   * 更改商家评价状态 oprStatus=ACITVE：启用评价 oprStatus=INACTIVE：禁用评价
   * 
   * @param Ids 评论ids
   * @param oprStatus 操作
   * @return
   */
  Boolean changeEvaluateStatus(Long[] Ids, CommonStatus oprStatus);
}

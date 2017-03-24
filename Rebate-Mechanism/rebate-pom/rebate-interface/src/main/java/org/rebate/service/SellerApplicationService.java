package org.rebate.service;

import org.rebate.entity.SellerApplication;
import org.rebate.framework.service.BaseService;
import org.rebate.json.request.SellerRequest;

public interface SellerApplicationService extends BaseService<SellerApplication, Long> {

  /**
   * 创建商铺申请请求
   * 
   * @param req
   * @return
   */
  SellerApplication createApplication(SellerRequest req);
}

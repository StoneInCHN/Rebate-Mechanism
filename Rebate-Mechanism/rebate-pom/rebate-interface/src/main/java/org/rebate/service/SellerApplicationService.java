package org.rebate.service;

import org.rebate.entity.EndUser;
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
  SellerApplication createApplication(SellerRequest req, EndUser sellerUser);

  /**
   * 根据营业执照号获取商户申请信息
   * 
   * @param license
   * @return
   */
  SellerApplication findSellerApplicationBylicense(String license);
}

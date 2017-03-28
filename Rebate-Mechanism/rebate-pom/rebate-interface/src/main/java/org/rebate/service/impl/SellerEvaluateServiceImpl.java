package org.rebate.service.impl;

import javax.annotation.Resource;

import org.rebate.dao.SellerEvaluateDao;
import org.rebate.entity.SellerEvaluate;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.SellerEvaluateService;
import org.springframework.stereotype.Service;

@Service("sellerEvaluateServiceImpl")
public class SellerEvaluateServiceImpl extends BaseServiceImpl<SellerEvaluate, Long> implements
    SellerEvaluateService {

  @Resource(name = "sellerEvaluateDaoImpl")
  private SellerEvaluateDao sellerEvaluateDao;

  @Resource(name = "sellerEvaluateDaoImpl")
  public void setBaseDao(SellerEvaluateDao sellerEvaluateDao) {
    super.setBaseDao(sellerEvaluateDao);
  }

  @Override
  public SellerEvaluate getEvaluateByOrder(Long orderId) {
    return sellerEvaluateDao.getEvaluateByOrder(orderId);
  }
}

package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.SellerEvaluate;
import org.rebate.dao.SellerEvaluateDao;
import org.rebate.service.SellerEvaluateService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("sellerEvaluateServiceImpl")
public class SellerEvaluateServiceImpl extends BaseServiceImpl<SellerEvaluate,Long> implements SellerEvaluateService {

      @Resource(name="sellerEvaluateDaoImpl")
      public void setBaseDao(SellerEvaluateDao sellerEvaluateDao) {
         super.setBaseDao(sellerEvaluateDao);
  }
}
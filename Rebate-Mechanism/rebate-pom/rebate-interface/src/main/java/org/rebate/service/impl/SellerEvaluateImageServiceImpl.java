package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.SellerEvaluateImage;
import org.rebate.dao.SellerEvaluateImageDao;
import org.rebate.service.SellerEvaluateImageService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("sellerEvaluateImageServiceImpl")
public class SellerEvaluateImageServiceImpl extends BaseServiceImpl<SellerEvaluateImage,Long> implements SellerEvaluateImageService {

      @Resource(name="sellerEvaluateImageDaoImpl")
      public void setBaseDao(SellerEvaluateImageDao sellerEvaluateImageDao) {
         super.setBaseDao(sellerEvaluateImageDao);
  }
}
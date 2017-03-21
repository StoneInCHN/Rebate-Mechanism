package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.SellerEnvImage;
import org.rebate.dao.SellerEnvImageDao;
import org.rebate.service.SellerEnvImageService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("sellerEnvImageServiceImpl")
public class SellerEnvImageServiceImpl extends BaseServiceImpl<SellerEnvImage,Long> implements SellerEnvImageService {

      @Resource(name="sellerEnvImageDaoImpl")
      public void setBaseDao(SellerEnvImageDao sellerEnvImageDao) {
         super.setBaseDao(sellerEnvImageDao);
  }
}
package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.SellerCommitmentImage;
import org.rebate.dao.SellerCommitmentImageDao;
import org.rebate.service.SellerCommitmentImageService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("sellerCommitmentImageServiceImpl")
public class SellerCommitmentImageServiceImpl extends BaseServiceImpl<SellerCommitmentImage,Long> implements SellerCommitmentImageService {

      @Resource(name="sellerCommitmentImageDaoImpl")
      public void setBaseDao(SellerCommitmentImageDao sellerCommitmentImageDao) {
         super.setBaseDao(sellerCommitmentImageDao);
  }
}
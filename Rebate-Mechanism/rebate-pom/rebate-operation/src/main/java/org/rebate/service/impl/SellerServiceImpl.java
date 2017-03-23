package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.Seller;
import org.rebate.dao.SellerDao;
import org.rebate.service.SellerService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("sellerServiceImpl")
public class SellerServiceImpl extends BaseServiceImpl<Seller,Long> implements SellerService {

      @Resource(name="sellerDaoImpl")
      public void setBaseDao(SellerDao sellerDao) {
         super.setBaseDao(sellerDao);
  }
}
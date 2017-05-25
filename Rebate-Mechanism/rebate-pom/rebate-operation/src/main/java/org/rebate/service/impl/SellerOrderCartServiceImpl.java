package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.SellerOrderCart;
import org.rebate.dao.SellerOrderCartDao;
import org.rebate.service.SellerOrderCartService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("sellerOrderCartServiceImpl")
public class SellerOrderCartServiceImpl extends BaseServiceImpl<SellerOrderCart,Long> implements SellerOrderCartService {

      @Resource(name="sellerOrderCartDaoImpl")
      public void setBaseDao(SellerOrderCartDao sellerOrderCartDao) {
         super.setBaseDao(sellerOrderCartDao);
  }
}
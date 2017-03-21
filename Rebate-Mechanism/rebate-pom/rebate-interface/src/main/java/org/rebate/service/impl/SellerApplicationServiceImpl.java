package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.SellerApplication;
import org.rebate.dao.SellerApplicationDao;
import org.rebate.service.SellerApplicationService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("sellerApplicationServiceImpl")
public class SellerApplicationServiceImpl extends BaseServiceImpl<SellerApplication,Long> implements SellerApplicationService {

      @Resource(name="sellerApplicationDaoImpl")
      public void setBaseDao(SellerApplicationDao sellerApplicationDao) {
         super.setBaseDao(sellerApplicationDao);
  }
}
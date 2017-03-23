package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.SellerCategory;
import org.rebate.dao.SellerCategoryDao;
import org.rebate.service.SellerCategoryService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("sellerCategoryServiceImpl")
public class SellerCategoryServiceImpl extends BaseServiceImpl<SellerCategory,Long> implements SellerCategoryService {

      @Resource(name="sellerCategoryDaoImpl")
      public void setBaseDao(SellerCategoryDao sellerCategoryDao) {
         super.setBaseDao(sellerCategoryDao);
  }
}
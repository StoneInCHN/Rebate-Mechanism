package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service; 
import org.rebate.entity.Seller;
import org.rebate.dao.SellerDao;
import org.rebate.service.SellerService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("sellerServiceImpl")
public class SellerServiceImpl extends BaseServiceImpl<Seller,Long> implements SellerService {
	
	  @Value("${ProjectUploadPath}")
	  private String projectUploadPath;
	  
	  @Value("${uploadPath}")
	  private String uploadPath;
	  
      @Resource(name="sellerDaoImpl")
      public void setBaseDao(SellerDao sellerDao) {
         super.setBaseDao(sellerDao);
      }

      @Override
      public String getDiskPath(String storePictureUrl) {
    	  if (storePictureUrl.startsWith(projectUploadPath)) {
    		  return storePictureUrl.replaceFirst(projectUploadPath, uploadPath);
    	  }
    	  return null;
      }
}
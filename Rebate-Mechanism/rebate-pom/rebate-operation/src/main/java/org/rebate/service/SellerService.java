package org.rebate.service; 

import org.rebate.entity.Seller;
import org.rebate.framework.service.BaseService;

public interface SellerService extends BaseService<Seller,Long>{

	String getDiskPath(String storePictureUrl);

}
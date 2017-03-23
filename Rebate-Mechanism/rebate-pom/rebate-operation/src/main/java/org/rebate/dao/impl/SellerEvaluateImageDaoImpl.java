package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.SellerEvaluateImage;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.SellerEvaluateImageDao;
@Repository("sellerEvaluateImageDaoImpl")
public class SellerEvaluateImageDaoImpl extends  BaseDaoImpl<SellerEvaluateImage,Long> implements SellerEvaluateImageDao {

}
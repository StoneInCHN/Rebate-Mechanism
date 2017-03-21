package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.SellerEnvImage;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.SellerEnvImageDao;
@Repository("sellerEnvImageDaoImpl")
public class SellerEnvImageDaoImpl extends  BaseDaoImpl<SellerEnvImage,Long> implements SellerEnvImageDao {

}
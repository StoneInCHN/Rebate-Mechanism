package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.SellerCommitmentImage;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.SellerCommitmentImageDao;
@Repository("sellerCommitmentImageDaoImpl")
public class SellerCommitmentImageDaoImpl extends  BaseDaoImpl<SellerCommitmentImage,Long> implements SellerCommitmentImageDao {

}
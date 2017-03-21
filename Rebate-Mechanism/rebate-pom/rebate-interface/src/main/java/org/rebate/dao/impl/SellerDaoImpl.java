package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.Seller;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.SellerDao;
@Repository("sellerDaoImpl")
public class SellerDaoImpl extends  BaseDaoImpl<Seller,Long> implements SellerDao {

}
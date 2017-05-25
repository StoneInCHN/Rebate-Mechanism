package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.SellerOrderCart;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.SellerOrderCartDao;
@Repository("sellerOrderCartDaoImpl")
public class SellerOrderCartDaoImpl extends  BaseDaoImpl<SellerOrderCart,Long> implements SellerOrderCartDao {

}
package org.rebate.dao.impl;

import org.rebate.dao.SellerOrderCartDao;
import org.rebate.entity.SellerOrderCart;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("sellerOrderCartDaoImpl")
public class SellerOrderCartDaoImpl extends BaseDaoImpl<SellerOrderCart, Long> implements SellerOrderCartDao {

}

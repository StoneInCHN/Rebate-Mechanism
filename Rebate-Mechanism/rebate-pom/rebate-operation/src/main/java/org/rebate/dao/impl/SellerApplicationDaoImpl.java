package org.rebate.dao.impl;

import org.rebate.dao.SellerApplicationDao;
import org.rebate.entity.SellerApplication;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("sellerApplicationDaoImpl")
public class SellerApplicationDaoImpl extends BaseDaoImpl<SellerApplication, Long> implements
    SellerApplicationDao {

}

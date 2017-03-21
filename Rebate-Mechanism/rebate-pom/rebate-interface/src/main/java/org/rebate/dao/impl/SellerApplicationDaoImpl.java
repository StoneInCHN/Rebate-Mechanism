package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.SellerApplication;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.SellerApplicationDao;
@Repository("sellerApplicationDaoImpl")
public class SellerApplicationDaoImpl extends  BaseDaoImpl<SellerApplication,Long> implements SellerApplicationDao {

}
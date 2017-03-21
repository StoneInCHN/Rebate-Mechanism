package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.SellerEvaluate;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.SellerEvaluateDao;
@Repository("sellerEvaluateDaoImpl")
public class SellerEvaluateDaoImpl extends  BaseDaoImpl<SellerEvaluate,Long> implements SellerEvaluateDao {

}
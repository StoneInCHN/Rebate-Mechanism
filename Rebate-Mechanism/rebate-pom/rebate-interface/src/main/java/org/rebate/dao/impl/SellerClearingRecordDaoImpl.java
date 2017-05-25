package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.SellerClearingRecord;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.SellerClearingRecordDao;
@Repository("sellerClearingRecordDaoImpl")
public class SellerClearingRecordDaoImpl extends  BaseDaoImpl<SellerClearingRecord,Long> implements SellerClearingRecordDao {

}
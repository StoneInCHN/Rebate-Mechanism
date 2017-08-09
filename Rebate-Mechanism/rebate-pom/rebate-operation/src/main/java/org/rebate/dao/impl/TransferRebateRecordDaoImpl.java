package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.TransferRebateRecord;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.TransferRebateRecordDao;
@Repository("transferRebateRecordDaoImpl")
public class TransferRebateRecordDaoImpl extends  BaseDaoImpl<TransferRebateRecord,Long> implements TransferRebateRecordDao {

}
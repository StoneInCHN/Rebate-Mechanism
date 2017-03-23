package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.RebateRecord;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.RebateRecordDao;
@Repository("rebateRecordDaoImpl")
public class RebateRecordDaoImpl extends  BaseDaoImpl<RebateRecord,Long> implements RebateRecordDao {

}
package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.SystemWithdrawRecord;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.SystemWithdrawRecordDao;
@Repository("systemWithdrawRecordDaoImpl")
public class SystemWithdrawRecordDaoImpl extends  BaseDaoImpl<SystemWithdrawRecord,Long> implements SystemWithdrawRecordDao {

}
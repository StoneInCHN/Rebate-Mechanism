package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.LeScoreRecord;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.LeScoreRecordDao;
@Repository("leScoreRecordDaoImpl")
public class LeScoreRecordDaoImpl extends  BaseDaoImpl<LeScoreRecord,Long> implements LeScoreRecordDao {

}
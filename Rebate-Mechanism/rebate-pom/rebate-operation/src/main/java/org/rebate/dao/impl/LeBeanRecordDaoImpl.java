package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.LeBeanRecord;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.LeBeanRecordDao;
@Repository("leBeanRecordDaoImpl")
public class LeBeanRecordDaoImpl extends  BaseDaoImpl<LeBeanRecord,Long> implements LeBeanRecordDao {

}
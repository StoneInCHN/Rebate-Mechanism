package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.LeMindRecord;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.LeMindRecordDao;
@Repository("leMindRecordDaoImpl")
public class LeMindRecordDaoImpl extends  BaseDaoImpl<LeMindRecord,Long> implements LeMindRecordDao {

}
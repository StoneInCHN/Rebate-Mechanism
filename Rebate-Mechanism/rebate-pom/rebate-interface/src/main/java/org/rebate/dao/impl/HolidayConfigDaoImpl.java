package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.HolidayConfig;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.HolidayConfigDao;
@Repository("holidayConfigDaoImpl")
public class HolidayConfigDaoImpl extends  BaseDaoImpl<HolidayConfig,Long> implements HolidayConfigDao {

}
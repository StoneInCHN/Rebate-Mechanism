package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.ThirdApiReport;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.ThirdApiReportDao;
@Repository("thirdApiReportDaoImpl")
public class ThirdApiReportDaoImpl extends  BaseDaoImpl<ThirdApiReport,Long> implements ThirdApiReportDao {

}
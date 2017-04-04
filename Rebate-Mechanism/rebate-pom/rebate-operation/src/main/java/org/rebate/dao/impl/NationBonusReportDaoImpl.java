package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.NationBonusReport;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.NationBonusReportDao;
@Repository("nationBonusReportDaoImpl")
public class NationBonusReportDaoImpl extends  BaseDaoImpl<NationBonusReport,Long> implements NationBonusReportDao {

}
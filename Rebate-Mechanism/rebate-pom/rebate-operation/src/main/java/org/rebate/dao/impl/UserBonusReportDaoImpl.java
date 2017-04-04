package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.UserBonusReport;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.UserBonusReportDao;
@Repository("userBonusReportDaoImpl")
public class UserBonusReportDaoImpl extends  BaseDaoImpl<UserBonusReport,Long> implements UserBonusReportDao {

}
package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.UserRegReport;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.UserRegReportDao;
@Repository("userRegReportDaoImpl")
public class UserRegReportDaoImpl extends  BaseDaoImpl<UserRegReport,Long> implements UserRegReportDao {

}
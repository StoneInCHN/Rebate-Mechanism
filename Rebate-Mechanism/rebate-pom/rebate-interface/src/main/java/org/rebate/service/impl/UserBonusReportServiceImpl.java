package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.UserBonusReport;
import org.rebate.dao.UserBonusReportDao;
import org.rebate.service.UserBonusReportService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("userBonusReportServiceImpl")
public class UserBonusReportServiceImpl extends BaseServiceImpl<UserBonusReport,Long> implements UserBonusReportService {

      @Resource(name="userBonusReportDaoImpl")
      public void setBaseDao(UserBonusReportDao userBonusReportDao) {
         super.setBaseDao(userBonusReportDao);
  }
}
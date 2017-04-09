package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.UserRegReport;
import org.rebate.dao.UserRegReportDao;
import org.rebate.service.UserRegReportService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("userRegReportServiceImpl")
public class UserRegReportServiceImpl extends BaseServiceImpl<UserRegReport,Long> implements UserRegReportService {

      @Resource(name="userRegReportDaoImpl")
      public void setBaseDao(UserRegReportDao userRegReportDao) {
         super.setBaseDao(userRegReportDao);
  }
}
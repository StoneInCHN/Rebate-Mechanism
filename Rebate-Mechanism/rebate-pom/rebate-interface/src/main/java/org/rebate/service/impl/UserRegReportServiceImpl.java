package org.rebate.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.rebate.dao.UserRegReportDao;
import org.rebate.entity.UserRegReport;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.UserRegReportService;
import org.springframework.stereotype.Service;

@Service("userRegReportServiceImpl")
public class UserRegReportServiceImpl extends BaseServiceImpl<UserRegReport, Long> implements
    UserRegReportService {
  @Resource(name = "userRegReportDaoImpl")
  private UserRegReportDao userRegReportDao;

  @Resource(name = "userRegReportDaoImpl")
  public void setBaseDao(UserRegReportDao userRegReportDao) {
    super.setBaseDao(userRegReportDao);
  }

  @Override
  public UserRegReport getRegReportByDate(Date date) {
    return userRegReportDao.getRegReportByDate(date);
  }
}

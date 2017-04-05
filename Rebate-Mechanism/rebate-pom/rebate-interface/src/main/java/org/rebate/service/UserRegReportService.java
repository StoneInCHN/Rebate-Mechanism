package org.rebate.service;

import java.util.Date;

import org.rebate.entity.UserRegReport;
import org.rebate.framework.service.BaseService;

public interface UserRegReportService extends BaseService<UserRegReport, Long> {
  /**
   * 根据时间获取用户注册统计信息
   * 
   * @param date
   * @return
   */
  UserRegReport getRegReportByDate(Date date);
}

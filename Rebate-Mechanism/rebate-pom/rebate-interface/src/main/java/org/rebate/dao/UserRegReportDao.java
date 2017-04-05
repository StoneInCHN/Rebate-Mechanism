package org.rebate.dao;

import java.util.Date;

import org.rebate.entity.UserRegReport;
import org.rebate.framework.dao.BaseDao;

public interface UserRegReportDao extends BaseDao<UserRegReport, Long> {
  /**
   * 根据时间获取用户注册统计信息
   * 
   * @param date
   * @return
   */
  UserRegReport getRegReportByDate(Date date);
}

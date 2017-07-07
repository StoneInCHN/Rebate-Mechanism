package org.rebate.dao;

import java.util.Date;

import org.rebate.entity.ThirdApiReport;
import org.rebate.framework.dao.BaseDao;

public interface ThirdApiReportDao extends BaseDao<ThirdApiReport, Long> {
  /**
   * 根据日期获取次数记录
   * 
   * @param curDate
   * @return
   */
  ThirdApiReport getApiReportByDate(Date curDate);
}

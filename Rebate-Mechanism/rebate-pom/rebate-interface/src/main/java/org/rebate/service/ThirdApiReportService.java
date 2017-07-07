package org.rebate.service;

import java.util.Date;

import org.rebate.entity.ThirdApiReport;
import org.rebate.framework.service.BaseService;

public interface ThirdApiReportService extends BaseService<ThirdApiReport, Long> {

  /**
   * 记录统计第三方api调用次数
   */
  void verifyBankCardReport();

  /**
   * 根据日期获取次数记录
   * 
   * @param curDate
   * @return
   */
  ThirdApiReport getApiReportByDate(Date curDate);
}

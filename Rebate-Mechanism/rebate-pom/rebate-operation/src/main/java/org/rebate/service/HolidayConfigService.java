package org.rebate.service;

import java.util.Date;

import org.rebate.entity.HolidayConfig;
import org.rebate.framework.service.BaseService;

public interface HolidayConfigService extends BaseService<HolidayConfig, Long> {

  /**
   * 判断当前日期是否为法定节假日
   * 
   * @param curDate
   * @return
   */
  Boolean isHoliday(Date curDate);
}

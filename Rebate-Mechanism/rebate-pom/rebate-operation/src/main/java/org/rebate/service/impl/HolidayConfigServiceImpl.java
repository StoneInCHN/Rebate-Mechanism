package org.rebate.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.dao.HolidayConfigDao;
import org.rebate.entity.HolidayConfig;
import org.rebate.entity.commonenum.CommonEnum.CommonStatus;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.HolidayConfigService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service("holidayConfigServiceImpl")
public class HolidayConfigServiceImpl extends BaseServiceImpl<HolidayConfig, Long> implements
    HolidayConfigService {


  @Resource(name = "holidayConfigDaoImpl")
  private HolidayConfigDao holidayConfigDao;

  @Resource(name = "holidayConfigDaoImpl")
  public void setBaseDao(HolidayConfigDao holidayConfigDao) {
    super.setBaseDao(holidayConfigDao);
  }

  @Override
  public Boolean isHoliday(Date curDate) {
    List<Filter> filters = new ArrayList<Filter>();
    filters.add(Filter.eq("status", CommonStatus.ACITVE));
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.asc("startDate"));
    List<HolidayConfig> holidayConfigs = holidayConfigDao.findList(null, null, filters, orderings);
    if (CollectionUtils.isEmpty(holidayConfigs)) {
      return false;
    }
    for (HolidayConfig holidayConfig : holidayConfigs) {
      if (curDate.getTime() >= holidayConfig.getStartDate().getTime()
          && curDate.getTime() <= holidayConfig.getEndDate().getTime()) {
        return true;
      }
    }
    return false;
  }
}

package org.rebate.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.dao.ThirdApiReportDao;
import org.rebate.entity.ThirdApiReport;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.ordering.Ordering.Direction;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.ThirdApiReportService;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service("thirdApiReportServiceImpl")
public class ThirdApiReportServiceImpl extends BaseServiceImpl<ThirdApiReport, Long> implements
    ThirdApiReportService {

  @Resource(name = "thirdApiReportDaoImpl")
  private ThirdApiReportDao thirdApiReportDao;

  @Resource(name = "thirdApiReportDaoImpl")
  public void setBaseDao(ThirdApiReportDao thirdApiReportDao) {
    super.setBaseDao(thirdApiReportDao);
  }

  @Override
  public void verifyBankCardReport() {
    Date cur = TimeUtils.formatDate2Day0(new Date());
    ThirdApiReport report = getApiReportByDate(cur);
    if (report != null) {
      report.setVerifyBankCardCount(report.getVerifyBankCardCount() + 1);
      report.setVerifyBankCardTotalCount(report.getVerifyBankCardTotalCount() + 1);
    } else {
      report = new ThirdApiReport();
      report.setStatisticsDate(cur);
      report.setVerifyBankCardCount(1);
      List<Ordering> orders = new ArrayList<Ordering>();
      orders.add(new Ordering("statisticsDate", Direction.desc));
      List<ThirdApiReport> apiReports = thirdApiReportDao.findList(0, 1, null, orders);
      if (CollectionUtils.isEmpty(apiReports)) {
        report.setVerifyBankCardTotalCount(1);
      } else {
        report.setVerifyBankCardTotalCount(apiReports.get(0).getVerifyBankCardTotalCount() + 1);
      }
    }
    thirdApiReportDao.merge(report);
  }

  @Override
  public ThirdApiReport getApiReportByDate(Date curDate) {
    return thirdApiReportDao.getApiReportByDate(curDate);
  }
}

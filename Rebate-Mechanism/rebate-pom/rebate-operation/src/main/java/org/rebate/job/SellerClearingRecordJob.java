package org.rebate.job;

import java.util.Date;

import javax.annotation.Resource;

import org.rebate.service.SellerClearingRecordService;
import org.rebate.utils.DateUtils;
import org.rebate.utils.LogUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 商户货款结算Job(T+1后结算)
 *
 */
@Component("sellerClearingRecordJob")
@Lazy(false)
public class SellerClearingRecordJob {

  public static Date date;

  @Resource(name = "sellerClearingRecordServiceImpl")
  private SellerClearingRecordService sellerClearingRecordService;


  // @Scheduled(cron="0 30 21 * * ?")
  // @Scheduled(cron = "${job.daily_sellerClearing_cal.cron }")// 每天2点0分0秒执行 0 0 2 * * ?
  public void sellerClearingCalculate() {
    if (date == null) {
      date = new Date();
    }
    Date startDate = DateUtils.startOfDay(date);
    Date endDate = DateUtils.endOfDay(date);

    LogUtil.debug(this.getClass(), "sellerClearingCalculate", "Clearing Job Start! Time Period:"
        + startDate + " - " + endDate);

    try {
      sellerClearingRecordService.sellerClearing(startDate, endDate);
    } catch (Exception e) {
      date = null;
      e.printStackTrace();
    }

    LogUtil.debug(this.getClass(), "sellerClearingCalculate", "Clearing Job End! Time Period:"
        + startDate + " - " + endDate);
    date = null;
  }

}

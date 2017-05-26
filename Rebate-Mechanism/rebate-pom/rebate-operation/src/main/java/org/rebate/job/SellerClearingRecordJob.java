package org.rebate.job;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.rebate.service.SellerClearingRecordService;
import org.rebate.utils.LogUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/**
 * 商户货款结算Job(T+1后结算)
 *
 */
@Component
@Lazy(false)
public class SellerClearingRecordJob {


  @Resource(name = "sellerClearingRecordServiceImpl")
  private SellerClearingRecordService sellerClearingRecordService;


  //@Scheduled(cron="0/10 * *  * * ? ") //每10秒执行一次
  @Scheduled(cron = "${job.daily_sellerClearing_cal.cron }")// 每天2点0分0秒执行 0 0 2 * * ?
  public void sellerClearingCalculate() {

    Calendar startTime = Calendar.getInstance();
    startTime.setTime(new Date());
    startTime.add(Calendar.DATE, -1);
    startTime.set(Calendar.HOUR_OF_DAY, 0);
    startTime.set(Calendar.MINUTE, 0);
    startTime.set(Calendar.SECOND, 0);
    startTime.set(Calendar.MILLISECOND, 0);

    Calendar endTime = Calendar.getInstance();
    endTime.setTime(new Date());
    endTime.add(Calendar.DATE, -1);
    endTime.set(Calendar.HOUR_OF_DAY, 23);
    endTime.set(Calendar.MINUTE, 59);
    endTime.set(Calendar.SECOND, 59);
    endTime.set(Calendar.MILLISECOND, 999);

    LogUtil.debug(
        SellerClearingRecordJob.class,
        "sellerClearingCalculate",
        "Clearing Job Start! Time Period:" + startTime.getTime() + "-"
            + endTime.getTime());
    sellerClearingRecordService.sellerClearing(startTime.getTime(), endTime.getTime());
    LogUtil.debug(
        SellerClearingRecordJob.class,
        "sellerClearingCalculate",
        "Clearing Job End! Time Period:" + startTime.getTime() + "-"
            + endTime.getTime());
  }
}

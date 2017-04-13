package org.rebate.job;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.rebate.service.EndUserService;
import org.rebate.utils.LogUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class DailyLeScoreCalJob {


  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;


  // @Scheduled(cron="0/10 * *  * * ? ") //每10秒执行一次
  @Scheduled(cron = "${job.daily_leScore_cal.cron}")
  // 每天2点0分0秒执行 0 0 2 * * ?
  public void dailyLeScoreCalculate() {

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

    LogUtil.debug(DailyLeScoreCalJob.class, "dailyLeScoreCalculate",
        "daily user leScore calculate start! Date:" + startTime + "-" + endTime);

    endUserService.dailyBonusCalJob(startTime.getTime(), endTime.getTime());
    LogUtil.debug(DailyLeScoreCalJob.class, "dailyLeScoreCalculate",
        "daily user leScore calculate end! Date:" + startTime + "-" + endTime);
  }
}

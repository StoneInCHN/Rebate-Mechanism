package org.rebate.job;

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
  @Scheduled(cron = "59 59 23 * * ?")
  // 每天23点59分59秒执行
  public void dailyLeScoreCalculate() {

    LogUtil.debug(DailyLeScoreCalJob.class, "dailyLeScoreCalculate",
        "daily user leScore calculate start !");

    endUserService.dailyBonusCalJob();
    LogUtil.debug(DailyLeScoreCalJob.class, "dailyLeScoreCalculate",
        "daily user leScore calculate end!");
  }
}

package org.rebate.job;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.rebate.service.EndUserService;
import org.rebate.service.MailService;
import org.rebate.utils.LogUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class JobUserBonusReportRun implements Job {
  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;

  @Resource(name = "mailServiceImpl")
  private MailService mailService;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    endUserService.callProcedure("pr_user_bonus_report");

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
        JobUserBonusReportRun.class,
        "JobUserBonusReportRun",
        "daily user bonus report start! Time Period:" + startTime.getTime() + "-"
            + endTime.getTime());
    try {
      endUserService.callProcedure("pr_nation_bonus_report");
    } catch (Exception e) {
      mailService.send("sujinxuan123@163.com,sj_msc@163.com",
          "yxsh:Call pr_nation_bonus_report faild!", e.getMessage());
    }

    LogUtil
        .debug(
            JobUserBonusReportRun.class,
            "JobUserBonusReportRun",
            "daily user bonus report end! Time Period:" + startTime.getTime() + "-"
                + endTime.getTime());
  }
}

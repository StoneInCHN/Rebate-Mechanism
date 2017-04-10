package org.rebate.job;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.rebate.service.EndUserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class JobRun implements Job {
  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;
  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    endUserService.callProcedure("pr_user_bonus_report");
  }
}

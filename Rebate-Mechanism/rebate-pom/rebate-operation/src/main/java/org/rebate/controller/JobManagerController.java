package org.rebate.controller;

import org.rebate.controller.base.BaseController;
import org.rebate.job.JobRun;
import org.rebate.service.QuartzManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("JobManagerController")
@RequestMapping("/console/job")
public class JobManagerController extends BaseController {

  @Autowired
  QuartzManagerService quartzManagerService;

  /**
   * 列表
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public @ResponseBody String add() {
    quartzManagerService.addJob("test", JobRun.class, "0/10 * * * * ?");

    return "success";
  }
}

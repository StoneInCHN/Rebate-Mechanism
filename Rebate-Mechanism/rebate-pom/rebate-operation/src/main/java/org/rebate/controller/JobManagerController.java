package org.rebate.controller;

import org.quartz.Job;
import org.rebate.controller.base.BaseController;
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
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public @ResponseBody String add(String name, String className, String cron) {
    Class<Job> clazz = null;
    try {
      clazz = (Class<Job>) Class.forName(className);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    quartzManagerService.addJob(name, clazz, cron);
    return "success";
  }

  @RequestMapping(value = "/remove", method = RequestMethod.POST)
  public @ResponseBody String add(String name) {
    quartzManagerService.removeJob(name);
    return "success";
  }
}

package org.rebate.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.Job;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.job.SellerClearingRecordJob;
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
  
  @Resource(name = "sellerClearingRecordJob")
  private SellerClearingRecordJob sellerClearingRecordJob;

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
  /**
   * 手动结算商家货款记录
   * @param manualDate
   * @return
   */
  @RequestMapping(value = "/manualClearingRecordJob", method = RequestMethod.POST)
  public @ResponseBody Message manualReconciliationJob(Date manualDate) {
      try {
          SellerClearingRecordJob.date = manualDate;
          sellerClearingRecordJob.sellerClearingCalculate();
          return SUCCESS_MESSAGE;
      } catch (Exception e) {
          return ERROR_MESSAGE;
      }
  }
}

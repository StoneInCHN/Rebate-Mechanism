package org.rebate.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.Job;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.job.AreaConsumeReportJob;
import org.rebate.job.LeScoreRecordJob;
import org.rebate.job.SellerClearingRecordJob;
import org.rebate.service.LeScoreRecordService;
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
  
  @Resource(name = "areaConsumeReportJob")
  private AreaConsumeReportJob areaConsumeReportJob;
  
  @Resource(name = "leScoreRecordServiceImpl")
  private LeScoreRecordService leScoreRecordService;
  
  @Resource(name = "leScoreRecordJob")
  private LeScoreRecordJob leScoreRecordJob;
  
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
          return Message.error(e.getMessage());
      }
  }
  /**
   * 手动统计地区交易额数据
   * @param manualDate
   * @return
   */
  @RequestMapping(value = "/manualAreaConsumeJob", method = RequestMethod.POST)
  public @ResponseBody Message manualAreaConsumeJob(Date manualDate) {
      try {
    	  AreaConsumeReportJob.date = manualDate;
          areaConsumeReportJob.areaConsumeReport();
          return SUCCESS_MESSAGE;
      } catch (Exception e) {
          return Message.error(e.getMessage());
      }
  }
  /**
   * 手动查询代付交易结果并更新乐分记录
   * @param manualDate
   * @return
   */
  @RequestMapping(value = "/manualQueryLeScoreJob", method = RequestMethod.POST)
  public @ResponseBody Message manualAreaConsumeJob(String reqSn) {
      try {
    	  if (reqSn != null) {
    	      leScoreRecordJob.updateRecordStatus(reqSn);
    	      return Message.success("手动查询代付交易结果并更新乐分记录!");
    	    } else {
    	      return Message.success("reqSn参数为空!");
    	    }
      } catch (Exception e) {
          return Message.error(e.getMessage());
      }
  }
}

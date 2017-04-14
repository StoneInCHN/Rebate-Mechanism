package org.rebate.controller;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.rebate.controller.base.BaseController;
import org.rebate.service.EndUserService;
import org.rebate.service.LeMindRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller("testController")
@RequestMapping("/test")
public class TestController extends BaseController {

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;

  @Resource(name = "leMindRecordServiceImpl")
  private LeMindRecordService leMindRecordService;

  /**
   * 测试
   * 
   * @param id
   * @param model
   * @return
   */
  @RequestMapping(value = "/test", method = RequestMethod.GET)
  public @ResponseBody String test(Date reqTime) {
    System.out.println(reqTime);
    Calendar startTime = Calendar.getInstance();
    startTime.setTime(reqTime);
    startTime.set(Calendar.HOUR_OF_DAY, 0);
    startTime.set(Calendar.MINUTE, 0);
    startTime.set(Calendar.SECOND, 0);
    startTime.set(Calendar.MILLISECOND, 0);

    Calendar endTime = Calendar.getInstance();
    endTime.setTime(new Date());
    endTime.set(Calendar.HOUR_OF_DAY, 23);
    endTime.set(Calendar.MINUTE, 59);
    endTime.set(Calendar.SECOND, 59);
    endTime.set(Calendar.MILLISECOND, 999);
    endUserService.dailyBonusCalJob(startTime.getTime(), endTime.getTime());
    // List<EndUser> list = endUserService.getMindUsersByDay(startTime.getTime(),
    // endTime.getTime());
    // LeMindRecord leMindRecord = leMindRecordService.find(new Long(1));
    // EndUser endUser = leMindRecord.getEndUser();
    // System.out.println(endUser.getCurLeMind());
    // endUser.setCurLeMind(endUser.getCurLeMind().add(new BigDecimal("10")));
    // System.out.println(endUser.getCurLeMind());
    // LeMindRecord leMindRecord1 = leMindRecordService.find(new Long(1));
    // System.out.println(leMindRecord1.getEndUser().getCurLeMind());
    return null;
  }


}

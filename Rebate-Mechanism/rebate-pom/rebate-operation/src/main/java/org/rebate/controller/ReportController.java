package org.rebate.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.controller.base.BaseController;
import org.rebate.entity.UserBonusReport;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.service.NationBonusReportService;
import org.rebate.service.UserBonusReportService;
import org.rebate.service.UserRegReportService;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("reportController")
@RequestMapping("/console/report")
public class ReportController extends BaseController {

  @Resource(name = "nationBonusReportServiceImpl")
  private NationBonusReportService nationBonusReportService;

  @Resource(name = "userBonusReportServiceImpl")
  private UserBonusReportService userBonusReportService;

  @Resource(name = "userRegReportServiceImpl")
  private UserRegReportService userRegReportService;

  /**
   * 列表
   */
  @RequestMapping(value = "/nationBonusReport", method = RequestMethod.GET)
  public String nationBonusReport(Pageable pageable, Date reportDateFrom, Date reportDateTo,
      ModelMap model) {

    List<Filter> filters = new ArrayList<Filter>();
    Filter dateFrom = new Filter("reportDate", Operator.ge, reportDateFrom);
    Filter dateTo = new Filter("reportDate", Operator.le, reportDateTo);
    filters.add(dateFrom);
    filters.add(dateTo);
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("reportDateFrom", reportDateFrom);
    model.addAttribute("reportDateTo", reportDateTo);
    model.addAttribute("page", nationBonusReportService.findPage(pageable));
    return "/report/nationBonusReport";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/userBonusReport", method = RequestMethod.GET)
  public String userBonusReport(Pageable pageable, String nickName, String mobile,
      Date reportDateFrom, Date reportDateTo, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    Filter dateFrom = new Filter("reportDate", Operator.ge, reportDateFrom);
    Filter dateTo = new Filter("reportDate", Operator.le, reportDateTo);
    if (nickName != null) {
      Filter nickNameFilter = new Filter("userId&nickName", Operator.like, "%" + nickName + "%");
      filters.add(nickNameFilter);
    }
    if (mobile != null) {
      Filter mobileFilter = new Filter("userId&cellPhoneNum", Operator.like, "%" + mobile + "%");
      filters.add(mobileFilter);
    }

    filters.add(dateFrom);
    filters.add(dateTo);
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("reportDateFrom", reportDateFrom);
    model.addAttribute("reportDateTo", reportDateTo);
    model.addAttribute("mobile", mobile);
    model.addAttribute("nickName", nickName);
    model.addAttribute("page", userBonusReportService.findPage(pageable));
    return "/report/userBonusReport";
  }

  /**
   * 用户统计数据
   */
  @RequestMapping(value = "/userBonusReportData", method = RequestMethod.GET)
  public @ResponseBody List<UserBonusReport> userBonusReport(String nickName, String mobile,
      Date reportDateFrom, Date reportDateTo, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    Filter dateFrom = new Filter("reportDate", Operator.ge, reportDateFrom);
    Filter dateTo = new Filter("reportDate", Operator.le, reportDateTo);
    if (nickName != null) {
      Filter nickNameFilter = new Filter("userId&cellPhoneNum", Operator.like, "%"+nickName+"%");
      filters.add(nickNameFilter);
    }
    if (mobile != null) {
      Filter mobileFilter = new Filter("userId&cellPhoneNum", Operator.like, "%" + mobile + "%");
      filters.add(mobileFilter);
    }

    filters.add(dateFrom);
    filters.add(dateTo);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));

    return userBonusReportService.findList(null, filters, orderings);
  }

  /**
   * 用户注册统计列表
   */
  @RequestMapping(value = "/userRegReport", method = RequestMethod.GET)
  public String list(Pageable pageable, Date reportDateFrom, Date reportDateTo, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (reportDateFrom != null) {
      filters.add(Filter.ge("statisticsDate", TimeUtils.formatDate2Day(reportDateFrom)));
      model.addAttribute("reportDateFrom", reportDateFrom);
    }
    if (reportDateTo != null) {
      filters.add(Filter.le("statisticsDate", TimeUtils.formatDate2Day(reportDateTo)));
      model.addAttribute("reportDateTo", reportDateTo);
    }
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("statisticsDate"));
    pageable.setOrders(orderings);
    model.addAttribute("page", userRegReportService.findPage(pageable));
    return "/report/userRegReport";
  }
}

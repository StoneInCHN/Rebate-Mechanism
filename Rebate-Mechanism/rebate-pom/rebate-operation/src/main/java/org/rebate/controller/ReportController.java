package org.rebate.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.Seller;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.request.SellerRequest;
import org.rebate.service.NationBonusReportService;
import org.rebate.service.SellerCategoryService;
import org.rebate.service.SellerService;
import org.rebate.service.UserBonusReportService;
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
  public String userBonusReport(Pageable pageable, Date reportDateFrom, Date reportDateTo,
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
    model.addAttribute("page", userBonusReportService.findPage(pageable));
    return "/report/userBonusReport";
  }
}

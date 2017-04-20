package org.rebate.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.service.BonusParamPerDayService;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("bonusParamController")
@RequestMapping("/console/bonusParam")
public class BonusParamController extends BaseController {

  @Resource(name = "bonusParamPerDayServiceImpl")
  private BonusParamPerDayService bonusParamPerDayService;


  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, Date reqStartTime, Date reqEndTime, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (reqStartTime != null) {
      filters.add(Filter.ge("bonusDate", TimeUtils.formatDate2Day(reqStartTime)));
      model.addAttribute("reqStartTime", reqStartTime);
    }
    if (reqEndTime != null) {
      filters
          .add(Filter.lt("bonusDate", TimeUtils.addDays(1, TimeUtils.formatDate2Day(reqEndTime))));
      model.addAttribute("reqEndTime", reqEndTime);
    }
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("bonusDate"));
    pageable.setOrders(orderings);
    model.addAttribute("page", bonusParamPerDayService.findPage(pageable));
    return "/bonusParam/list";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    if (ids.length >= bonusParamPerDayService.count()) {
      return Message.error("rebate.common.deleteAllNotAllowed");
    }
    bonusParamPerDayService.delete(ids);
    return SUCCESS_MESSAGE;
  }

}

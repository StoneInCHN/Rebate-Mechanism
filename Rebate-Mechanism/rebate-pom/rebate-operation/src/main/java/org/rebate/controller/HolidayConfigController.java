package org.rebate.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.HolidayConfig;
import org.rebate.entity.commonenum.CommonEnum.CommonStatus;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering.Direction;
import org.rebate.framework.paging.Pageable;
import org.rebate.service.HolidayConfigService;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("console/holidayConfig")
@Controller("holidayConfigController")
public class HolidayConfigController extends BaseController {


  @Resource(name = "holidayConfigServiceImpl")
  private HolidayConfigService holidayConfigService;

  /**
   * 添加
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add() {
    return "/holidayConfig/add";
  }

  /**
   * 保存
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public String save(HolidayConfig holidayConfig) {
    if (!isValid(holidayConfig)) {
      return ERROR_VIEW;
    }
    if (holidayConfig.getStartDate() == null || holidayConfig.getEndDate() == null) {
      return ERROR_VIEW;
    }
    holidayConfig.setStatus(CommonStatus.ACITVE);
    holidayConfigService.save(holidayConfig);
    return "redirect:list.jhtml";
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("holidayConfig", holidayConfigService.find(id));
    return "/holidayConfig/edit";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String update(HolidayConfig holidayConfig) {
    if (!isValid(holidayConfig)) {
      return ERROR_VIEW;
    }
    holidayConfigService.update(holidayConfig);
    return "redirect:list.jhtml";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, CommonStatus status, String holidayName, Date startDate,
      Date endDate, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (StringUtils.isNotEmpty(holidayName)) {
      filters.add(Filter.like("holidayName", "%" + holidayName + "%"));
      model.addAttribute("holidayName", holidayName);
    }
    if (status != null) {
      filters.add(Filter.eq("status", status));
      model.addAttribute("status", status);
    }
    if (startDate != null) {
      filters.add(Filter.ge("startDate", TimeUtils.formatDate2Day(startDate)));
      model.addAttribute("startDate", startDate);
    }
    if (endDate != null) {
      filters.add(Filter.le("endDate", TimeUtils.formatDate2Day(endDate)));
      model.addAttribute("endDate", endDate);
    }
    pageable.setOrderProperty("startDate");
    pageable.setOrderDirection(Direction.asc);
    pageable.setFilters(filters);
    model.addAttribute("page", holidayConfigService.findPage(pageable));
    return "/holidayConfig/list";
  }

  /**
   * 禁用
   */
  @RequestMapping(value = "/locked", method = RequestMethod.POST)
  public @ResponseBody Message locked(Long[] ids) {
    List<HolidayConfig> holidays = new ArrayList<HolidayConfig>();
    if (ids != null && ids.length > 0) {
      for (Long id : ids) {
        HolidayConfig holiday = holidayConfigService.find(id);
        holiday.setStatus(CommonStatus.INACTIVE);
        holidays.add(holiday);
      }
    }
    holidayConfigService.update(holidays);
    return SUCCESS_MESSAGE;
  }


  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    if (ids != null && ids.length > 0) {
      holidayConfigService.delete(ids);
    }
    return SUCCESS_MESSAGE;
  }

}

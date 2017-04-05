package org.rebate.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.paging.Pageable;
import org.rebate.service.OperationLogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/console/operationLog")
public class OperationLogController extends BaseController {

  @Resource(name = "operationLogServiceImpl")
  private OperationLogService operationLogService;

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable,String operator, Date beginDate, Date endDate, ModelMap model) {

    List<Filter> filters = new ArrayList<Filter>();
    
    if (operator != null) {
      Filter operatorFilter = new Filter("operator", Operator.like, operator);
      filters.add(operatorFilter);
    }
    
    if (beginDate != null) {
      Filter dateGeFilter = new Filter("createDate", Operator.ge, beginDate);
      filters.add(dateGeFilter);
    }
    if (endDate != null) {
      Filter dateLeFilter = new Filter("createDate", Operator.le, endDate);
      filters.add(dateLeFilter);
    }
    pageable.setFilters(filters);
    model.addAttribute("page", operationLogService.findPage(pageable));
    model.addAttribute("beginDate", beginDate);
    model.addAttribute("endDate", endDate);
    model.addAttribute("operator", operator);
    return "/operationLog/list";
  }

  /**
   * 查看
   */
  @RequestMapping(value = "/view", method = RequestMethod.GET)
  public String view(Long id, ModelMap model) {
    model.addAttribute("log", operationLogService.find(id));
    return "/operationLog/view";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    operationLogService.delete(ids);
    return SUCCESS_MESSAGE;
  }
}

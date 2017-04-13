package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.AgentCommissionConfig;
import org.rebate.entity.Area;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.paging.Pageable;
import org.rebate.service.AgentCommissionConfigService;
import org.rebate.service.AreaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("console/agentCommission")
@Controller("agentCommissionConfigController")
public class AgentCommissionConfigController extends BaseController {

  @Resource(name = "agentCommissionConfigServiceImpl")
  private AgentCommissionConfigService agentCommissionConfigService;

  @Resource(name = "areaServiceImpl")
  private AreaService areaService;

  /**
   * 添加
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add() {
    return "/agentCommission/add";
  }

  /**
   * 保存
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public String save(Long areaId, AgentCommissionConfig agentCommissionConfig) {
    if (!isValid(agentCommissionConfig)) {
      return ERROR_VIEW;
    }
    Area area = areaService.find(areaId);
    agentCommissionConfig.setArea(area);
    agentCommissionConfigService.save(agentCommissionConfig);
    return "redirect:list.jhtml";
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("agentCommission", agentCommissionConfigService.find(id));
    return "/agentCommission/edit";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String update(Long areaId, AgentCommissionConfig agentCommissionConfig) {
    if (!isValid(agentCommissionConfig)) {
      return ERROR_VIEW;
    }
    Area area = areaService.find(areaId);
    agentCommissionConfig.setArea(area);
    agentCommissionConfigService.update(agentCommissionConfig);
    return "redirect:list.jhtml";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable,String areaName, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if(StringUtils.isNoneBlank(areaName)){
      filters.add(Filter.like("area&fullName", areaName));
      model.addAttribute("areaName", areaName);
    }
    
    pageable.setFilters(filters);
    model.addAttribute("page", agentCommissionConfigService.findPage(pageable));
    return "/agentCommission/list";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    agentCommissionConfigService.delete(ids);
    return SUCCESS_MESSAGE;
  }

}

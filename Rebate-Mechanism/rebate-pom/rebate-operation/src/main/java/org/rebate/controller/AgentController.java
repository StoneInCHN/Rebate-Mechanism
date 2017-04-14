package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.Agent;
import org.rebate.entity.Area;
import org.rebate.entity.EndUser;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.paging.Pageable;
import org.rebate.service.AgentService;
import org.rebate.service.AreaService;
import org.rebate.service.EndUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("agentController")
@RequestMapping("console/agent")
public class AgentController extends BaseController {

  @Resource(name = "agentServiceImpl")
  private AgentService agentService;

  @Resource(name = "areaServiceImpl")
  private AreaService areaService;

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;

  /**
   * 添加
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add() {
    return "/agent/add";
  }

  /**
   * 保存
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public String save(Long areaId, Long endUserId, Agent agent) {
    if (!isValid(agent)) {
      return ERROR_VIEW;
    }
    Area area = areaService.find(areaId);
    EndUser endUser = endUserService.find(endUserId);
    if(area!=null&&endUser!=null){
      agent.setArea(area);
      agent.setEndUser(endUser);
      agentService.save(agent);
    }
    return "redirect:list.jhtml";
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("agent", agentService.find(id));
    return "/agent/edit";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String update(Long areaId, Long endUserId, Agent agent) {
    if (!isValid(agent)) {
      return ERROR_VIEW;
    }
    Area area = areaService.find(areaId);
    EndUser endUser = endUserService.find(endUserId);
    if(area!=null&&endUser!=null){
      agent.setArea(area);
      agent.setEndUser(endUser);
      agentService.update(agent);
    }
    return "redirect:list.jhtml";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, String areaName, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (StringUtils.isNoneBlank(areaName)) {
      filters.add(Filter.like("area&fullName", areaName));
      model.addAttribute("areaName", areaName);
    }
    pageable.setFilters(filters);
    model.addAttribute("page", agentService.findPage(pageable));
    return "/agent/list";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    agentService.delete(ids);
    return SUCCESS_MESSAGE;
  }

}
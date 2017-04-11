package org.rebate.controller;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.HotCity;
import org.rebate.framework.paging.Pageable;
import org.rebate.service.HotCityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("hotCityController")
@RequestMapping("/console/hotCity")
public class HotCityController extends BaseController{

  @Resource(name = "hotCityServiceImpl")
  private HotCityService hotCityService;
  
  /**
   * 添加
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add() {
    return "/hotCity/add";
  }

  /**
   * 保存
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public String save(HotCity hotCity) {
    if (!isValid(hotCity)) {
      return ERROR_VIEW;
    }
    hotCityService.save(hotCity);
    return "redirect:list.jhtml";
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("hotCity", hotCityService.find(id));
    return "/userHelp/edit";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String update(HotCity hotCity) {
    if (!isValid(hotCity)) {
      return ERROR_VIEW;
    }
    hotCityService.update(hotCity);
    return "redirect:list.jhtml";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model) {
    model.addAttribute("page", hotCityService.findPage(pageable));
    return "/userHelp/list";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    if (ids.length >= hotCityService.count()) {
      return Message.error("rebate.common.deleteAllNotAllowed");
    }
    hotCityService.delete(ids);
    return SUCCESS_MESSAGE;
  }

}

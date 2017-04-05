package org.rebate.controller;

import javax.annotation.Resource;

import org.rebate.entity.Admin;
import org.rebate.entity.SystemConfig;
import org.rebate.service.SystemConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("systemConfigController")
@RequestMapping("/console/systemConfig")
public class SystemConfigController {

  @Resource(name = "systemConfigServiceImpl")
  private SystemConfigService systemConfigService;

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("systemConfig", systemConfigService.find(id));
    return "/systemConfig/edit";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String update(SystemConfig config) {
    systemConfigService.update(config, "configKey","createDate");
    return "redirect:list.jhtml";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(ModelMap model) {
    model.addAttribute("systemConfigs", systemConfigService.findAll());
    return "/systemConfig/list";
  }

}

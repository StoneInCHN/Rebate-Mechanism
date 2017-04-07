package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.entity.SettingConfig;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.SettingConfigKey;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.service.SettingConfigService;
import org.rebate.service.SystemConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("systemConfigController")
@RequestMapping("/console/systemConfig")
public class SystemConfigController {

  @Resource(name = "systemConfigServiceImpl")
  private SystemConfigService systemConfigService;
  
  @Resource(name = "settingConfigServiceImpl")
  private SettingConfigService settingConfigService;

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("systemConfig", systemConfigService.find(id));
    return "/systemConfig/edit";
  }

  /**
   * 支付方式修改
   */
  @RequestMapping(value = "/editPay", method = RequestMethod.GET)
  public String editPay(Long id, ModelMap model) {
    model.addAttribute("systemConfig", systemConfigService.find(id));
    return "/systemConfig/editPay";
  }
  /**
   * 更新
   */
  @RequestMapping(value = "/updatePay", method = RequestMethod.POST)
  public String updatePay(SystemConfig config) {
    SystemConfig temp = systemConfigService.find(config.getId());
    temp.setIsEnabled(config.getIsEnabled());
    temp.setRemark(config.getRemark());
    systemConfigService.update(temp);
    return "redirect:list.jhtml";
  }
  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String update(SystemConfig config) {
    systemConfigService.update(config, "configKey", "createDate");
    return "redirect:list.jhtml";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(ModelMap model) {
    List<Filter> systemConfigsfilters = new ArrayList<Filter>();
    systemConfigsfilters.add(Filter.ne("configKey", SystemConfigKey.PAYMENTTYPE));
    model.addAttribute("systemConfigs",
        systemConfigService.findList(null, systemConfigsfilters, null));
    List<Filter> payMentTypeFilter = new ArrayList<Filter>();
    payMentTypeFilter.add(Filter.eq("configKey", SystemConfigKey.PAYMENTTYPE));
    model.addAttribute("paymentTypes",
        systemConfigService.findList(null, payMentTypeFilter, null));
    
    List<SettingConfig> settingConfigs =settingConfigService.findAll();
    if(settingConfigs!=null && settingConfigs.size() >0){
      for (SettingConfig settingConfig : settingConfigs) {
        if(SettingConfigKey.ABOUT_US == settingConfig.getConfigKey()){
          model.addAttribute("about",settingConfig);
        }else if (SettingConfigKey.HELP == settingConfig.getConfigKey()) {
          model.addAttribute("help",settingConfig);
        }else if (SettingConfigKey.LICENSE_AGREEMENT == settingConfig.getConfigKey()) {
          model.addAttribute("license",settingConfig);
        }
      }
    }
    return "/systemConfig/list";
  }

}

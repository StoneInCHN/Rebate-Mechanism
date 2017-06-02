package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.SettingConfig;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.SettingConfigKey;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering;
import org.rebate.service.SettingConfigService;
import org.rebate.service.SystemConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("systemConfigController")
@RequestMapping("/console/systemConfig")
public class SystemConfigController extends BaseController {

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
    if (temp.getConfigKey().equals(SystemConfigKey.PAYMENTTYPE)) {
      temp.setIsEnabled(config.getIsEnabled());
      temp.setRemark(config.getRemark());
      temp.setConfigOrder(config.getConfigOrder());
      systemConfigService.update(temp);
    }
    return "redirect:list.jhtml";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public @ResponseBody Message update(SystemConfig config) {
    SystemConfig temp =new SystemConfig();
    SystemConfig temp1 =  systemConfigService.find(config.getId());
    if (!temp1.getConfigKey().equals(SystemConfigKey.PAYMENTTYPE)) {
      temp.setIsEnabled(config.getIsEnabled());
      temp.setRemark(config.getRemark());
      temp.setConfigValue(config.getConfigValue());
      temp.setConfigKey(temp1.getConfigKey());
      temp.setCreateDate(temp1.getCreateDate());
      temp.setModifyDate(temp1.getModifyDate());
      temp.setId(config.getId());
      temp.setConfigOrder(temp1.getConfigOrder());
      return systemConfigService.updateSystemConfig(temp);
    }else {
      return ERROR_MESSAGE;
    }
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(ModelMap model) {
    List<Filter> systemConfigsfilters = new ArrayList<Filter>();
    systemConfigsfilters.add(Filter.ne("configKey", SystemConfigKey.PAYMENTTYPE));
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.asc("configOrder"));
    model.addAttribute("systemConfigs",
        systemConfigService.findList(null, systemConfigsfilters, orderings));
    List<Filter> payMentTypeFilter = new ArrayList<Filter>();
    payMentTypeFilter.add(Filter.eq("configKey", SystemConfigKey.PAYMENTTYPE));
    model.addAttribute("paymentTypes", systemConfigService.findList(null, payMentTypeFilter, null));
    List<SettingConfig> temps = new ArrayList<SettingConfig>();
    List<SettingConfig> descInfos = new ArrayList<SettingConfig>();
    List<SettingConfig> settingConfigs = settingConfigService.findAll();
    if (settingConfigs != null && settingConfigs.size() > 0) {
      for (SettingConfig settingConfig : settingConfigs) {
        if (SettingConfigKey.ABOUT_US == settingConfig.getConfigKey()) {
          model.addAttribute("about", settingConfig);
        } else if (SettingConfigKey.LICENSE_AGREEMENT == settingConfig.getConfigKey()) {
          model.addAttribute("license", settingConfig);
        }else if (SettingConfigKey.SELLER_PAYMENT_DESC == settingConfig.getConfigKey()) {
          descInfos.add(settingConfig);
        }else if (SettingConfigKey.USER_AUTH_DESC == settingConfig.getConfigKey()) {
          descInfos.add(settingConfig);
        }else if (SettingConfigKey.BANKCARD_MOBILE_DESC == settingConfig.getConfigKey()) {
          descInfos.add(settingConfig);
        }else if (SettingConfigKey.BANKCARD_OWNER_DESC == settingConfig.getConfigKey()) {
          descInfos.add(settingConfig);
        }else if (SettingConfigKey.BANKCARD_SERVICE_AGREEMENT == settingConfig.getConfigKey()) {
          descInfos.add(settingConfig);
        }else {
          temps.add(settingConfig);
        }
      }
    }
    model.addAttribute("descInfos", descInfos);
    model.addAttribute("settingConfigs", temps);
    return "/systemConfig/list";
  }


  /**
   * 修改许可协议
   */
  @RequestMapping(value = "/editSettingConfig", method = RequestMethod.GET)
  public String editSettingConfig(Long id, SettingConfigKey configKey, ModelMap model) {
    model.addAttribute("settingConfig", settingConfigService.find(id));
    model.addAttribute("configKey", configKey);
    return "/systemConfig/editSettingConfig";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/updateSettingConfig", method = RequestMethod.POST)
  public String updateSettingConfig(SettingConfig config) {
    SettingConfig temp = settingConfigService.find(config.getId());
    if (temp != null) {
      temp.setIsEnabled(config.getIsEnabled());
      temp.setConfigValue(config.getConfigValue());
      temp.setRemark(config.getRemark());
      settingConfigService.update(temp);
    } else {
      settingConfigService.save(config);
    }
    return "redirect:list.jhtml";
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/editOther", method = RequestMethod.GET)
  public String editOther(Long id, ModelMap model) {
    model.addAttribute("settingConfig", settingConfigService.find(id));
    return "/systemConfig/editOther";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/updateOther", method = RequestMethod.POST)
  public String updateOther(SettingConfig config) {
    SettingConfig temp = settingConfigService.find(config.getId());
    if (temp != null) {
      temp.setIsEnabled(config.getIsEnabled());
      temp.setRemark(config.getRemark());
      temp.setConfigValue(config.getConfigValue());
      settingConfigService.update(temp);
    }
    return "redirect:list.jhtml";
  }
  
  
  /**
   * 编辑
   */
  @RequestMapping(value = "/editDescInfo", method = RequestMethod.GET)
  public String editDescInfo(Long id, ModelMap model) {
    model.addAttribute("settingConfig", settingConfigService.find(id));
    return "/systemConfig/editDescInfo";
  }
  
  /**
   * 更新
   */
  @RequestMapping(value = "/updateDescInfo", method = RequestMethod.POST)
  public String updateDescInfo(SettingConfig config) {
    SettingConfig temp = settingConfigService.find(config.getId());
    if (temp != null) {
      temp.setIsEnabled(config.getIsEnabled());
      temp.setRemark(config.getRemark());
      temp.setConfigValue(config.getConfigValue());
      settingConfigService.update(temp);
    }
    return "redirect:list.jhtml";
  }
}

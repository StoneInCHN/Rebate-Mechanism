package org.rebate.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.rebate.beans.CommonAttributes;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.SettingConfig;
import org.rebate.entity.commonenum.CommonEnum.SettingConfigKey;
import org.rebate.json.base.ResponseOne;
import org.rebate.json.request.UserRequest;
import org.rebate.service.SettingConfigService;
import org.rebate.utils.FieldFilterUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 数据字典
 * 
 *
 */
@Controller("settingConfigController")
@RequestMapping("/settingConfig")
public class SettingConfigController extends MobileBaseController {

  @Resource(name = "settingConfigServiceImpl")
  private SettingConfigService settingConfigService;



  /**
   * 根据config key获取配置信息
   * 
   * @return
   */
  @RequestMapping(value = "/getConfigByKey", method = RequestMethod.POST)
  public @ResponseBody ResponseOne<Map<String, Object>> selectArea(@RequestBody UserRequest request) {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    // Long userId = request.getUserId();
    // String token = request.getToken();
    SettingConfigKey configKey = request.getConfigKey();


    SettingConfig config = settingConfigService.getConfigsByKey(configKey);
    String[] propertys = {"id", "configValue"};
    Map<String, Object> result = FieldFilterUtils.filterEntityMap(propertys, config);

    response.setMsg(result);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

}

package org.rebate.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.request.OrderRequest;
import org.rebate.service.EndUserService;
import org.rebate.service.SystemConfigService;
import org.rebate.utils.FieldFilterUtils;
import org.rebate.utils.TokenGenerator;
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
@Controller("systemConfigController")
@RequestMapping("/systemConfig")
public class SystemConfigController extends MobileBaseController {

  @Resource(name = "systemConfigServiceImpl")
  private SystemConfigService systemConfigService;

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;



  /**
   * 根据config key获取配置信息
   * 
   * @return
   */
  @RequestMapping(value = "/getConfigByKey", method = RequestMethod.POST)
  public @ResponseBody ResponseMultiple<Map<String, Object>> selectArea(
      @RequestBody OrderRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    SystemConfigKey configKey = request.getConfigKey();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    List<SystemConfig> configList = systemConfigService.getConfigsByKey(configKey);
    String[] propertys = {"id", "configValue"};
    List<Map<String, Object>> result = FieldFilterUtils.filterCollectionMap(propertys, configList);

    response.setMsg(result);
    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

}

package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.beans.CommonAttributes;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.SettingConfig;
import org.rebate.entity.UserHelp;
import org.rebate.entity.commonenum.CommonEnum.SettingConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.ordering.Ordering.Direction;
import org.rebate.json.base.BaseRequest;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.base.ResponseOne;
import org.rebate.json.request.UserRequest;
import org.rebate.service.SettingConfigService;
import org.rebate.service.UserHelpService;
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

  @Resource(name = "userHelpServiceImpl")
  private UserHelpService userHelpService;



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

    response.setDesc(config != null ? config.getRemark() : null);
    response.setMsg(result);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 用户帮助列表
   * 
   * @param request
   * @return
   */
  @RequestMapping(value = "/userHelpList", method = RequestMethod.POST)
  public @ResponseBody ResponseMultiple<Map<String, Object>> userHelpList(
      @RequestBody BaseRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();


    List<Filter> filters = new ArrayList<Filter>();
    Filter userFilter = new Filter("isEnabled", Operator.eq, true);
    filters.add(userFilter);

    List<Ordering> orders = new ArrayList<Ordering>();
    Ordering ordering = new Ordering("configOrder", Direction.asc);
    orders.add(ordering);

    List<UserHelp> list = userHelpService.findList(null, filters, orders);
    String[] propertys = {"id", "title"};
    List<Map<String, Object>> result = FieldFilterUtils.filterCollectionMap(propertys, list);

    response.setMsg(result);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 根据id获取用户帮助详情
   * 
   * @return
   */
  @RequestMapping(value = "/userHelpDetail", method = RequestMethod.POST)
  public @ResponseBody ResponseOne<Map<String, Object>> userHelpDetail(
      @RequestBody UserRequest request) {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long entityId = request.getEntityId();

    UserHelp userHelp = userHelpService.find(entityId);
    String[] propertys = {"id", "title", "content"};
    Map<String, Object> result = FieldFilterUtils.filterEntityMap(propertys, userHelp);
    response.setMsg(result);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

}

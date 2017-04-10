package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.Area;
import org.rebate.entity.HotCity;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.ordering.Ordering.Direction;
import org.rebate.json.base.BaseRequest;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.service.AreaService;
import org.rebate.service.EndUserService;
import org.rebate.service.HotCityService;
import org.rebate.utils.FieldFilterUtils;
import org.rebate.utils.TokenGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 城市
 * 
 *
 */
@Controller("areaController")
@RequestMapping("/area")
public class AreaController extends MobileBaseController {

  @Resource(name = "areaServiceImpl")
  private AreaService areaService;
  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;
  @Resource(name = "hotCityServiceImpl")
  private HotCityService hotCityService;


  /**
   * 选择所在地区
   * 
   * @return
   */
  @RequestMapping(value = "/selectArea", method = RequestMethod.POST)
  public @ResponseBody ResponseMultiple<Map<String, Object>> selectArea(
      @RequestBody BaseRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Long areaId = request.getEntityId();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    List<Filter> filters = new ArrayList<Filter>();
    if (areaId == null) {// 查询顶级地区
      Filter parentFilter = new Filter("parent", Operator.isNull, null);
      filters.add(parentFilter);
    } else {// 查询areaId下的子级地区
      Filter childFilter = new Filter("parent", Operator.eq, areaId);
      filters.add(childFilter);
    }

    List<Area> list = areaService.findList(null, filters, null);
    String[] propertys = {"id", "name"};
    List<Map<String, Object>> result = FieldFilterUtils.filterCollectionMap(propertys, list);
    response.setMsg(result);

    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }


  /**
   * 获取热门城市
   * 
   * @return
   */
  @RequestMapping(value = "/getHotCity", method = RequestMethod.POST)
  public @ResponseBody ResponseMultiple<Map<String, Object>> getHotCity(
      @RequestBody BaseRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();


    List<Ordering> orderings = new ArrayList<Ordering>();
    Ordering ordering = new Ordering("cityOrder", Direction.asc);
    orderings.add(ordering);

    List<HotCity> list = hotCityService.findList(null, null, orderings);
    String[] propertys = {"cityId", "cityName"};
    List<Map<String, Object>> result = FieldFilterUtils.filterCollectionMap(propertys, list);
    response.setMsg(result);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }


}

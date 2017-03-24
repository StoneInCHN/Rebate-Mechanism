package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.beans.CommonAttributes;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.Area;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.json.base.BaseRequest;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.service.AreaService;
import org.rebate.service.EndUserService;
import org.rebate.utils.FieldFilterUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 城市
 * 
 * @author sujinxuan
 *
 */
@Controller("areaController")
@RequestMapping("/areaController")
public class AreaController extends MobileBaseController {

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "endUserServiceImpl")
	private EndUserService endUserService;

	/**
	 * 获取省/市/区
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getCity", method = RequestMethod.POST)
	public @ResponseBody ResponseMultiple<Map<String, Object>> getArea(
			@RequestBody BaseRequest request) {

		ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

		Long userId = request.getUserId();
		String token = request.getToken();
		// Long areaId = request.getAreaId();

		// 验证登录token
		// String userToken = endUserService.getEndUserToken(userId);
		// if (!TokenGenerator.isValiableToken(token, userToken)) {
		// response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
		// response.setDesc(Message.error("csh.user.token.timeout")
		// .getContent());
		// return response;
		// }

		List<Filter> filters = new ArrayList<Filter>();
		Filter cityFilter = new Filter("isCity", Operator.eq, true);
		filters.add(cityFilter);

		List<Area> list = areaService.findList(null, filters, null);
		String[] propertys = { "id", "name" };
		List<Map<String, Object>> result = FieldFilterUtils
				.filterCollectionMap(propertys, list);

		response.setMsg(result);
		// String newtoken = TokenGenerator.generateToken(request.getToken());
		// endUserService.createEndUserToken(newtoken, userId);
		// response.setToken(newtoken);
		response.setCode(CommonAttributes.SUCCESS);
		return response;
	}

	/**
	 * 获取省/市/区
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getHotCity", method = RequestMethod.POST)
	public @ResponseBody ResponseMultiple<Map<String, Object>> getHotCity(
			@RequestBody BaseRequest request) {

		ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

		Long userId = request.getUserId();
		String token = request.getToken();
		// Long areaId = request.getAreaId();

		// 验证登录token
		// String userToken = endUserService.getEndUserToken(userId);
		// if (!TokenGenerator.isValiableToken(token, userToken)) {
		// response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
		// response.setDesc(Message.error("csh.user.token.timeout")
		// .getContent());
		// return response;
		// }

		List<Filter> filters = new ArrayList<Filter>();
		Filter cityFilter = new Filter("isCity", Operator.eq, true);
		filters.add(cityFilter);
		Filter hotCityFilter = new Filter("isHotCity", Operator.eq, true);
		filters.add(hotCityFilter);

		List<Area> list = areaService.findList(null, filters, null);
		String[] propertys = { "id", "name" };
		List<Map<String, Object>> result = FieldFilterUtils
				.filterCollectionMap(propertys, list);

		response.setMsg(result);
		// String newtoken = TokenGenerator.generateToken(request.getToken());
		// endUserService.createEndUserToken(newtoken, userId);
		// response.setToken(newtoken);
		response.setCode(CommonAttributes.SUCCESS);
		return response;
	}
}

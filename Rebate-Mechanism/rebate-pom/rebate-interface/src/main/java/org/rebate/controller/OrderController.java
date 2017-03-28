package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.EndUser;
import org.rebate.entity.Order;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.json.base.BaseRequest;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.base.ResponseOne;
import org.rebate.service.EndUserService;
import org.rebate.service.OrderService;
import org.rebate.utils.FieldFilterUtils;
import org.rebate.utils.TokenGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 订单
 * 
 * @author huyong
 *
 */
@Controller("orderController")
@RequestMapping("/orderController")
public class OrderController extends MobileBaseController {

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "endUserServiceImpl")
	private EndUserService endUserService;

	/**
	 * 获取用户订单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getOrderUnderUser", method = RequestMethod.POST)
	public @ResponseBody ResponseMultiple<Map<String, Object>> getOrderUnderUser(
			@RequestBody BaseRequest request) {

		ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

		Long userId = request.getUserId();
		String token = request.getToken();
		EndUser endUser = endUserService.find(userId);

		// 验证登录token
		String userToken = endUserService.getEndUserToken(userId);
		if (!TokenGenerator.isValiableToken(token, userToken)) {
			response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
			response.setDesc(Message.error("rebate.user.token.timeout")
					.getContent());
			return response;
		}

		List<Filter> filters = new ArrayList<Filter>();
		Filter endUserFilter = new Filter("endUser", Operator.eq, endUser);
		filters.add(endUserFilter);

		List<Order> list = orderService.findList(null, filters, null);
		String[] propertys = { "id", "sn", "seller.name", "userScore",
				"amount", "createDate", "evaluate", "remark",
				"evaluate.content", "evaluate.sellerReply" };
		List<Map<String, Object>> result = FieldFilterUtils
				.filterCollectionMap(propertys, list);

		response.setMsg(result);
		String newtoken = TokenGenerator.generateToken(request.getToken());
		endUserService.createEndUserToken(newtoken, userId);
		response.setToken(newtoken);
		response.setCode(CommonAttributes.SUCCESS);
		return response;
	}

	/**
	 * 获取订单详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "/orderDetail", method = RequestMethod.POST)
	public @ResponseBody ResponseOne<Map<String, Object>> orderDetail(
			@RequestBody BaseRequest request) {

		ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

		Long userId = request.getUserId();
		String token = request.getToken();
		Long orderId = request.getEntityId();

		// 验证登录token
		// String userToken = endUserService.getEndUserToken(userId);
		// if (!TokenGenerator.isValiableToken(token, userToken)) {
		// response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
		// response.setDesc(Message.error("csh.user.token.timeout")
		// .getContent());
		// return response;
		// }

		Order order = orderService.find(orderId);
		String[] propertys = { "id", "seller.name", "userScore", "amount",
				"evaluate.content", "evaluate.sellerReply" };
		Map<String, Object> result = FieldFilterUtils.filterEntityMap(
				propertys, order);

		response.setMsg(result);
		// String newtoken = TokenGenerator.generateToken(request.getToken());
		// endUserService.createEndUserToken(newtoken, userId);
		// response.setToken(newtoken);
		response.setCode(CommonAttributes.SUCCESS);
		return response;
	}
}

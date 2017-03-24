package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.common.log.LogUtil;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.SellerCategory;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.ordering.Ordering.Direction;
import org.rebate.json.base.BaseRequest;
import org.rebate.json.base.BaseResponse;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.request.SellerRequest;
import org.rebate.service.EndUserService;
import org.rebate.service.SellerApplicationService;
import org.rebate.service.SellerCategoryService;
import org.rebate.service.SellerService;
import org.rebate.utils.FieldFilterUtils;
import org.rebate.utils.TokenGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller("sellerController")
@RequestMapping("/seller")
public class SellerController extends MobileBaseController {

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;

  @Resource(name = "sellerServiceImpl")
  private SellerService sellerService;

  @Resource(name = "sellerApplicationServiceImpl")
  private SellerApplicationService sellerApplicationService;

  @Resource(name = "sellerCategoryServiceImpl")
  private SellerCategoryService sellerCategoryService;


  /**
   * 获取店铺的行业类别
   * 
   * @return
   */
  @RequestMapping(value = "/getSellerCategory", method = RequestMethod.POST)
  public @ResponseBody ResponseMultiple<Map<String, Object>> selectArea(
      @RequestBody BaseRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    List<Filter> filters = new ArrayList<Filter>();
    Filter activeFilter = new Filter("isActive", Operator.eq, true);
    filters.add(activeFilter);

    List<Ordering> orderings = new ArrayList<Ordering>();
    Ordering ordering = new Ordering("categorOrder", Direction.asc);
    orderings.add(ordering);

    List<SellerCategory> list = sellerCategoryService.findList(null, filters, orderings);
    String[] propertys = {"id", "categoryName"};
    List<Map<String, Object>> result = FieldFilterUtils.filterCollectionMap(propertys, list);
    response.setMsg(result);

    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }


  /**
   * 申请店铺
   *
   * @param req
   * @return
   */
  @RequestMapping(value = "/apply", method = RequestMethod.POST)
  public @ResponseBody BaseResponse apply(SellerRequest req) {
    BaseResponse response = new BaseResponse();

    Long userId = req.getUserId();
    String token = req.getToken();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    sellerApplicationService.createApplication(req);
    if (LogUtil.isDebugEnabled(SellerController.class)) {
      LogUtil
          .debug(
              SellerController.class,
              "apply",
              "apply seller. userId: %s, contactCellPhone: %s, sellerName: %s, categoryId: %s, discount: %s, storePhone: %s, areaId: %s, address: %s, licenseNum: %s, latitude: %s, longitude: %s",
              userId, req.getContactCellPhone(), req.getSellerName(), req.getCategoryId(),
              req.getDiscount(), req.getStorePhone(), req.getAreaId(), req.getAddress(),
              req.getLicenseNum(), req.getLatitude(), req.getLongitude());
    }

    response.setCode(CommonAttributes.SUCCESS);
    String newtoken = TokenGenerator.generateToken(req.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    return response;
  }

}

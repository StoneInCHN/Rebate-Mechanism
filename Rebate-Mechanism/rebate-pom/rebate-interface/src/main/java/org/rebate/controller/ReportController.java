package org.rebate.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.common.log.LogUtil;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.NationBonusReport;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerCategory;
import org.rebate.entity.SellerEnvImage;
import org.rebate.entity.SellerEvaluate;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.UserBonusReport;
import org.rebate.entity.commonenum.CommonEnum.FeaturedService;
import org.rebate.entity.commonenum.CommonEnum.SortType;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.ordering.Ordering.Direction;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.rebate.json.base.BaseRequest;
import org.rebate.json.base.BaseResponse;
import org.rebate.json.base.PageResponse;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.base.ResponseOne;
import org.rebate.json.request.SellerRequest;
import org.rebate.service.EndUserService;
import org.rebate.service.NationBonusReportService;
import org.rebate.service.SellerApplicationService;
import org.rebate.service.SellerCategoryService;
import org.rebate.service.SellerEvaluateService;
import org.rebate.service.SellerJdbcService;
import org.rebate.service.SellerService;
import org.rebate.service.SystemConfigService;
import org.rebate.service.UserBonusReportService;
import org.rebate.utils.FieldFilterUtils;
import org.rebate.utils.LatLonUtil;
import org.rebate.utils.QRCodeGenerator;
import org.rebate.utils.TokenGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller("reportController")
@RequestMapping("/report")
public class ReportController extends MobileBaseController {

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;

  @Resource(name = "userBonusReportServiceImpl")
  private UserBonusReportService userBonusReportService;
  @Resource(name = "nationBonusReportServiceImpl")
  private NationBonusReportService nationBonusReportService;

  /**
   * 获取用户分红信息
   * 
   * @return
   */
  @RequestMapping(value = "/getUserBonusReport", method = RequestMethod.POST)
  public @ResponseBody ResponseOne<Map<String, Object>> getUserBonusReport(
      @RequestBody BaseRequest request) {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

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
    Filter userFilter = new Filter("userId", Operator.eq, userId);
    filters.add(userFilter);

    List<Ordering> orderings = new ArrayList<Ordering>();
    Ordering ordering = new Ordering("reportDate", Direction.desc);
    orderings.add(ordering);

    List<UserBonusReport> list = userBonusReportService.findList(1, filters, orderings);
    String[] propertys =
        {"id", "bonusLeScore", "consumeTotalAmount", "highBonusLeScore", "reportDate"};
    if (list != null && list.size() > 0) {
      Map<String, Object> result = FieldFilterUtils.filterEntityMap(propertys, list.get(0));
      response.setMsg(result);
    }

    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 获取全国分红信息
   * 
   * @return
   */
  @RequestMapping(value = "/getNationBonusReport", method = RequestMethod.POST)
  public @ResponseBody ResponseOne<Map<String, Object>> getNationBonusReport(
      @RequestBody BaseRequest request) {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

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

    List<Ordering> orderings = new ArrayList<Ordering>();
    Ordering ordering = new Ordering("reportDate", Direction.desc);
    orderings.add(ordering);

    List<NationBonusReport> list = nationBonusReportService.findList(1, filters, orderings);
    String[] propertys =
        {"id", "consumeTotalAmount", "consumePeopleNum", "sellerNum", "publicTotalAmount",
            "leMindByDay", "consumeByDay", "bonusLeScoreByDay", "publicAmountByDay", "reportDate"};
    if (list != null && list.size() > 0) {
      Map<String, Object> result = FieldFilterUtils.filterEntityMap(propertys, list.get(0));
      response.setMsg(result);
    }

    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }
}

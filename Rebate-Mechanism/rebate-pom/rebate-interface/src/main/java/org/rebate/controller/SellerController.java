package org.rebate.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.rebate.aspect.UserParam.CheckUserType;
import org.rebate.aspect.UserValidCheck;
import org.rebate.beans.CommonAttributes;
import org.rebate.common.log.LogUtil;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerCategory;
import org.rebate.entity.SellerEnvImage;
import org.rebate.entity.SellerEvaluate;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.CommonStatus;
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
import org.rebate.service.SellerApplicationService;
import org.rebate.service.SellerCategoryService;
import org.rebate.service.SellerEvaluateService;
import org.rebate.service.SellerJdbcService;
import org.rebate.service.SellerService;
import org.rebate.service.SystemConfigService;
import org.rebate.utils.FieldFilterUtils;
import org.rebate.utils.LatLonUtil;
import org.rebate.utils.QRCodeGenerator;
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

  @Resource(name = "sellerJdbcServiceImpl")
  private SellerJdbcService sellerJdbcService;

  @Resource(name = "sellerEvaluateServiceImpl")
  private SellerEvaluateService sellerEvaluateService;

  @Resource(name = "systemConfigServiceImpl")
  private SystemConfigService systemConfigService;


  /**
   * 获取店铺的行业类别
   * 
   * @return
   */
  @RequestMapping(value = "/getSellerCategory", method = RequestMethod.POST)
  public @ResponseBody ResponseMultiple<Map<String, Object>> getSellerCategory(
      @RequestBody BaseRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    // Long userId = request.getUserId();
    // String token = request.getToken();
    //
    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }

    List<Filter> filters = new ArrayList<Filter>();
    Filter activeFilter = new Filter("isActive", Operator.eq, true);
    filters.add(activeFilter);

    List<Ordering> orderings = new ArrayList<Ordering>();
    Ordering ordering = new Ordering("categorOrder", Direction.asc);
    orderings.add(ordering);

    List<SellerCategory> list = sellerCategoryService.findList(null, filters, orderings);
    String[] propertys = {"id", "categoryName", "categoryPicUrl"};
    List<Map<String, Object>> result = FieldFilterUtils.filterCollectionMap(propertys, list);
    response.setMsg(result);

    // String newtoken = TokenGenerator.generateToken(request.getToken());
    // endUserService.createEndUserToken(newtoken, userId);
    // response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }


  /**
   * 首页商户列表
   * 
   * @return
   */
  @RequestMapping(value = "/list", method = RequestMethod.POST)
  public @ResponseBody ResponseMultiple<Map<String, Object>> list(@RequestBody SellerRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    // Long userId = request.getUserId();
    // String token = request.getToken();
    Integer pageSize = request.getPageSize();
    Integer pageNumber = request.getPageNumber();
    Integer radius = setting.getSearchRadius();
    String latitude = request.getLatitude();// 纬度
    String longitude = request.getLongitude();// 经度
    Long categoryId = request.getCategoryId();
    String areaIds = request.getAreaIds();
    FeaturedService featuredService = request.getFeaturedService();
    SortType sortType = request.getSortType();
    String keyWord = request.getKeyWord();

    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }


    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);

    if (LogUtil.isDebugEnabled(SellerController.class)) {
      LogUtil
          .debug(
              SellerController.class,
              "list",
              "seller list. radius: %s,latitude: %s, longitude: %s, categoryId: %s, areaIds: %s, featuredService: %s, sortType: %s, keyWord: %s, pageSize: %s, pageNumber: %s",
              radius, latitude, longitude, categoryId, areaIds, featuredService, sortType, keyWord,
              pageSize, pageNumber);
    }
    Page<Map<String, Object>> page =
        sellerJdbcService.getSellerList(longitude, latitude, pageable, radius, categoryId, areaIds,
            featuredService, sortType, keyWord);

    PageResponse pageInfo = new PageResponse();
    pageInfo.setPageNumber(pageNumber);
    pageInfo.setPageSize(pageSize);
    pageInfo.setTotal((int) page.getTotal());
    response.setPage(pageInfo);
    response.setMsg(page.getContent());

    // String newtoken = TokenGenerator.generateToken(request.getToken());
    // endUserService.createEndUserToken(newtoken, userId);
    // response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 首页商家列表详情
   *
   * @param req
   * @return
   */
  @RequestMapping(value = "/detail", method = RequestMethod.POST)
  public @ResponseBody ResponseOne<Map<String, Object>> detail(@RequestBody SellerRequest req) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long userId = req.getUserId();
    Long sellerId = req.getEntityId();
    String latitude = req.getLatitude();// 纬度
    String longitude = req.getLongitude();// 经度
    Seller seller = sellerService.find(sellerId);

    String[] properties =
        {"id", "name", "storePictureUrl", "address", "storePhone", "businessTime", "avgPrice",
            "sellerCategory.categoryName", "latitude", "longitude", "description", "discount",
            "favoriteNum", "featuredService", "rateScore"};
    List<String> envImgs = new ArrayList<String>();
    for (SellerEnvImage envImage : seller.getEnvImages()) {
      envImgs.add(envImage.getSource());
    }
    Map<String, Object> map = FieldFilterUtils.filterEntityMap(properties, seller);
    map.put("envImgs", envImgs);
    map.put("userCollected", false);
    if (userId != null && sellerService.userCollectSeller(userId, sellerId) != null) {
      map.put("userCollected", true);
    }
    SystemConfig rebateScore = systemConfigService.getConfigByKey(SystemConfigKey.REBATESCORE_USER);
    SystemConfig unitConsume = systemConfigService.getConfigByKey(SystemConfigKey.UNIT_CONSUME);
    BigDecimal unit = new BigDecimal(unitConsume.getConfigValue());
    map.put("unitConsume", unit);
    BigDecimal rebateUserScore =
        unit.subtract(seller.getDiscount().divide(new BigDecimal("10")).multiply(unit)).multiply(
            new BigDecimal(rebateScore.getConfigValue()));
    if (unit.compareTo(rebateUserScore) < 0) {
      map.put("rebateScore", unit);
    } else {
      map.put("rebateScore", rebateUserScore);
    }

    if (longitude != null && latitude != null) {
      map.put("distance", LatLonUtil
          .getPointDistance(new Double(longitude), new Double(latitude), new Double(seller
              .getLongitude().toString()), new Double(seller.getLatitude().toString())));
    } else {
      map.put("distance", null);
    }

    response.setMsg(map);

    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 商户评价列表
   * 
   * @return
   */
  @RequestMapping(value = "/evaluateList", method = RequestMethod.POST)
  public @ResponseBody ResponseMultiple<Map<String, Object>> evaluateList(
      @RequestBody SellerRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    // Long userId = request.getUserId();
    // String token = request.getToken();
    Integer pageSize = request.getPageSize();
    Integer pageNumber = request.getPageNumber();
    Long sellerId = request.getSellerId();

    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }


    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);

    if (LogUtil.isDebugEnabled(SellerController.class)) {
      LogUtil.debug(SellerController.class, "evaluateList", "seller evaluate list. sellerId: %s",
          sellerId, pageSize, pageNumber);
    }
    Seller seller = sellerService.find(sellerId);
    List<Filter> filters = new ArrayList<Filter>();
    Filter sellerFilter = new Filter("seller", Operator.eq, seller);
    Filter statusFilter = new Filter("status", Operator.eq, CommonStatus.ACITVE);
    filters.add(statusFilter);
    filters.add(sellerFilter);
    pageable.setFilters(filters);
    pageable.setOrderDirection(Direction.desc);
    pageable.setOrderProperty("createDate");

    Page<SellerEvaluate> page = sellerEvaluateService.findPage(pageable);
    String[] propertys =
        {"id", "endUser.userPhoto", "endUser.nickName", "createDate", "content", "evaluateImages",
            "sellerReply", "score"};
    List<Map<String, Object>> result =
        FieldFilterUtils.filterCollectionMap(propertys, page.getContent());

    PageResponse pageInfo = new PageResponse();
    pageInfo.setPageNumber(pageNumber);
    pageInfo.setPageSize(pageSize);
    pageInfo.setTotal((int) page.getTotal());
    response.setPage(pageInfo);
    response.setMsg(result);

    // String newtoken = TokenGenerator.generateToken(request.getToken());
    // endUserService.createEndUserToken(newtoken, userId);
    // response.setToken(newtoken);
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
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody BaseResponse apply(SellerRequest req) {
    BaseResponse response = new BaseResponse();

    Long userId = req.getUserId();
    String token = req.getToken();

    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }

    // // 验证店铺折扣,不得低于设定的最小值
    // if (new BigDecimal(req.getDiscount()).compareTo(new
    // BigDecimal(setting.getSellerDiscountMin())) < 0) {
    // response.setCode(CommonAttributes.FAIL_COMMON);
    // response.setDesc(Message.error("rebate.seller.apply.discountMin",
    // setting.getSellerDiscountMin()).getContent());
    // return response;
    // }

    sellerApplicationService.createApplication(req);
    if (LogUtil.isDebugEnabled(SellerController.class)) {
      LogUtil
          .debug(
              SellerController.class,
              "apply",
              "apply seller. applyId: %s,userId: %s, contactCellPhone: %s, sellerName: %s, categoryId: %s, discount: %s, storePhone: %s, areaId: %s, address: %s, licenseNum: %s, latitude: %s, longitude: %s",
              req.getApplyId(), userId, req.getContactCellPhone(), req.getSellerName(),
              req.getCategoryId(), req.getDiscount(), req.getStorePhone(), req.getAreaId(),
              req.getAddress(), req.getLicenseNum(), req.getLatitude(), req.getLongitude());
    }

    response.setCode(CommonAttributes.SUCCESS);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    return response;
  }


  /**
   * 用户获取我的店铺信息
   *
   * @param req
   * @return
   */
  @RequestMapping(value = "/getSellerInfo", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.SELLER)
  public @ResponseBody ResponseOne<Map<String, Object>> getSellerInfo(@RequestBody BaseRequest req) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    Long userId = req.getUserId();
    String token = req.getToken();

    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }


    Seller seller = sellerService.findSellerByUser(userId);

    String[] properties =
        {"id", "name", "storePictureUrl", "address", "storePhone", "businessTime", "avgPrice",
            "area.id", "latitude", "longitude", "description", "discount", "favoriteNum",
            "featuredService", "totalOrderNum", "totalOrderAmount", "unClearingAmount"};
    List<String> envImgs = new ArrayList<String>();
    for (SellerEnvImage envImage : seller.getEnvImages()) {
      envImgs.add(envImage.getSource());
    }
    Map<String, Object> map = FieldFilterUtils.filterEntityMap(properties, seller);
    map.put("envImgs", envImgs);
    response.setMsg(map);

    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }


  /**
   * 修改完善店铺信息
   *
   * @param req
   * @return
   */
  @RequestMapping(value = "/editInfo", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.SELLER)
  public @ResponseBody BaseResponse editInfo(SellerRequest req) {

    BaseResponse response = new BaseResponse();

    Long userId = req.getUserId();
    String token = req.getToken();

    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }

    sellerService.editInfo(req);
    if (LogUtil.isDebugEnabled(SellerController.class)) {
      LogUtil
          .debug(
              SellerController.class,
              "editInfo",
              "Edit Seller Info. sellerId: %s,discount: %s, areaId: %s,avgPrice: %s, businessTime: %s, storePhone: %s, sellerName: %s, address: %s, featuredService: %s,latitude: %s, longitude: %s",
              req.getSellerId(), req.getDiscount(), req.getAreaId(), req.getAvgPrice(),
              req.getBusinessTime(), req.getStorePhone(), req.getSellerName(), req.getAddress(),
              req.getFeaturedService(), req.getLatitude(), req.getLongitude());
    }

    response.setCode(CommonAttributes.SUCCESS);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    return response;
  }

  /**
   * 收款二维码
   *
   * @param req
   * @return
   */
  @RequestMapping(value = "/getQrCode", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.SELLER)
  public @ResponseBody ResponseOne<Map<String, Object>> getQrCode(@RequestBody BaseRequest req) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();
    Long userId = req.getUserId();
    String token = req.getToken();
    Long sellerId = req.getEntityId();

    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }

    Seller seller = sellerService.find(sellerId);
    Map<String, Object> map = new HashMap<String, Object>();
    if (seller.getQrImage() == null) {
      String content =
          "{\"flag\":\"" + DigestUtils.md5Hex("翼享生活") + "\",\"sellerId\":\"" + sellerId + "\"}";
      byte[] bytes = QRCodeGenerator.generateQrImage(content);
      seller.setQrImage(bytes);
      sellerService.update(seller);
    }

    String[] properties = {"id", "name", "storePictureUrl", "area.fullName", "qrImage"};
    map = FieldFilterUtils.filterEntityMap(properties, seller);
    response.setMsg(map);

    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

}

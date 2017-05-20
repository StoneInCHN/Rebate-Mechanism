package org.rebate.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.aspect.UserValidCheck;
import org.rebate.aspect.UserParam.CheckUserType;
import org.rebate.beans.CommonAttributes;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.EndUser;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerOrderCart;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.rebate.json.base.PageResponse;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.base.ResponseOne;
import org.rebate.json.request.OrderRequest;
import org.rebate.json.request.SellerOrderCartRequest;
import org.rebate.service.EndUserService;
import org.rebate.service.OrderService;
import org.rebate.service.SellerOrderCartService;
import org.rebate.service.SellerService;
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
@Controller("sellerOrderCartController")
@RequestMapping("/sellerOrderCart")
public class SellerOrderCartController extends MobileBaseController {

  @Resource(name = "sellerOrderCartServiceImpl")
  private SellerOrderCartService sellerOrderCartService;

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;
  @Resource(name = "sellerServiceImpl")
  private SellerService sellerService;

  @Resource(name = "orderServiceImpl")
  private OrderService orderService;

  /**
   * 获取录单购物车
   * 
   * @return
   */
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, Object>> add(@RequestBody OrderRequest request) {

    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long userId = request.getUserId();
    Long entityId = request.getEntityId();
    String token = request.getToken();
    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }
    Seller seller = sellerService.find(request.getSellerId());
    BigDecimal tenBigDecimal = new BigDecimal(10);
    SellerOrderCart sellerOrderCart = new SellerOrderCart();
    sellerOrderCart.setAmount(request.getAmount());
    sellerOrderCart.setRebateAmount(request.getAmount().multiply(
        (tenBigDecimal.subtract(seller.getDiscount())).divide(new BigDecimal("10"))));
    sellerOrderCart.setSeller(seller);
    sellerOrderCart.setEndUser(endUserService.find(entityId));
    sellerOrderCartService.save(sellerOrderCart);


    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 获取录单购物车
   * 
   * @return
   */
  @RequestMapping(value = "/list", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.SELLER)
  public @ResponseBody ResponseMultiple<Map<String, Object>> list(@RequestBody OrderRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    Integer pageSize = request.getPageSize();
    Integer pageNumber = request.getPageNumber();
    // // 验证登录token
    // String userToken = endUserService.getEndUserToken(userId);
    // if (!TokenGenerator.isValiableToken(token, userToken)) {
    // response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
    // response.setDesc(Message.error("rebate.user.token.timeout").getContent());
    // return response;
    // }
    EndUser endUser = endUserService.find(userId);
    Seller seller = endUser.getSellers().iterator().next();
    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);
    List<Filter> filters = new ArrayList<>();


    Filter userFilter = new Filter("seller", Operator.eq, seller);
    filters.add(userFilter);
    pageable.setFilters(filters);
    Page<SellerOrderCart> sellerOrderCartPage = sellerOrderCartService.findPage(pageable);

    String[] propertys =
        {"id", "endUser.nickName", "endUser.cellPhoneNum", "amount", "rebateAmount", "createDate"};
    List<Map<String, Object>> result =
        FieldFilterUtils.filterCollectionMap(propertys, sellerOrderCartPage.getContent());

    PageResponse pageInfo = new PageResponse();
    pageInfo.setPageNumber(pageNumber);
    pageInfo.setPageSize(pageSize);
    pageInfo.setTotal((int) sellerOrderCartPage.getTotal());
    response.setPage(pageInfo);
    response.setMsg(result);

    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  @RequestMapping(value = "/confirmOrder", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, Object>> confirmOrder(
      @RequestBody SellerOrderCartRequest request) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();

    List<Long> deleteList = new ArrayList<>();

    for (OrderRequest orderRequest : request.getOrderRequests()) {
      deleteList.add(orderRequest.getEntityId());
    }

    Long deleteLong[] = new Long[deleteList.size()];
    deleteLong = deleteList.toArray(deleteLong);

    List<SellerOrderCart> sellerOrderCarts = sellerOrderCartService.findList(deleteLong);

    orderService.createSellerOrder(sellerOrderCarts);
    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, Object>> delete(
      @RequestBody SellerOrderCartRequest request) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();

    List<Long> deleteList = new ArrayList<>();

    for (OrderRequest orderRequest : request.getOrderRequests()) {
      deleteList.add(orderRequest.getEntityId());
    }
    Long deleteLong[] = new Long[deleteList.size()];
    deleteLong = deleteList.toArray(deleteLong);
    sellerOrderCartService.delete(deleteLong);

    String newtoken = TokenGenerator.generateToken(request.getToken());
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }
}

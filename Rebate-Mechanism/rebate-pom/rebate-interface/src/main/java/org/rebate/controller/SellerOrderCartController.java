package org.rebate.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.aspect.UserParam.CheckUserType;
import org.rebate.aspect.UserValidCheck;
import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.EndUser;
import org.rebate.entity.Order;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerOrderCart;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.ordering.Ordering.Direction;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.rebate.json.base.BaseRequest;
import org.rebate.json.base.BaseResponse;
import org.rebate.json.base.PageResponse;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.base.ResponseOne;
import org.rebate.json.request.OrderRequest;
import org.rebate.service.EndUserService;
import org.rebate.service.OrderService;
import org.rebate.service.SellerOrderCartService;
import org.rebate.service.SellerService;
import org.rebate.utils.FieldFilterUtils;
import org.rebate.utils.TokenGenerator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 录单购物车
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
  @Resource(name = "taskExecutor")
  private TaskExecutor taskExecutor;

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

    if (entityId != null) {
      Seller seller = sellerService.find(request.getSellerId());
      BigDecimal tenBigDecimal = new BigDecimal(10);
      SellerOrderCart sellerOrderCart = new SellerOrderCart();
      sellerOrderCart.setAmount(request.getAmount());
      sellerOrderCart.setRebateAmount(request.getAmount().multiply(
          (tenBigDecimal.subtract(seller.getDiscount())).divide(new BigDecimal("10"))));
      sellerOrderCart.setSeller(seller);
      sellerOrderCart.setEndUser(endUserService.find(entityId));
      sellerOrderCartService.save(sellerOrderCart);
    }

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("count", sellerOrderCartService.count());

    response.setMsg(map);
    String newtoken = TokenGenerator.generateToken(token);
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

    EndUser endUser = endUserService.find(userId);
    Seller seller = endUser.getSellers().iterator().next();
    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);
    List<Filter> filters = new ArrayList<Filter>();
    Filter sellerFilter = new Filter("seller", Operator.eq, seller.getId());
    filters.add(sellerFilter);
    pageable.setFilters(filters);
    pageable.setOrderDirection(Direction.desc);
    pageable.setOrderProperty("createDate");

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

    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }


  /**
   * 从购物车提交录单
   * 
   * @param request
   * @return
   */
  @RequestMapping(value = "/confirmOrder", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseOne<Map<String, Object>> confirmOrder(
      @RequestBody BaseRequest request) {
    ResponseOne<Map<String, Object>> response = new ResponseOne<Map<String, Object>>();

    Long userId = request.getUserId();
    String token = request.getToken();
    List<Long> entityIds = request.getEntityIds();
    Long sellerId = request.getEntityId();

    if (CollectionUtils.isEmpty(entityIds)) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.seller.order.cart.isNull").getContent());
      return response;
    }

    Long entityIdsLong[] = new Long[entityIds.size()];
    entityIdsLong = entityIds.toArray(entityIdsLong);

    List<SellerOrderCart> sellerOrderCarts = sellerOrderCartService.findList(entityIdsLong);
    BigDecimal totalAmount = new BigDecimal("0");
    for (SellerOrderCart sellerOrderCart : sellerOrderCarts) {
      totalAmount = totalAmount.add(sellerOrderCart.getAmount());
    }

    if (orderService.isOverSellerLimitAmount(sellerId, totalAmount)) {
      response.setCode(CommonAttributes.FAIL_COMMON);
      response.setDesc(Message.error("rebate.payOrder.seller.limitAmount").getContent());
      return response;
    }
    List<Order> orders = orderService.createSellerOrder(sellerOrderCarts);

    taskExecutor.execute(new Runnable() {
      public void run() {
        orderService.updateSellerOrderBeforePay(orders.get(0).getBatchSn());
      }
    });
    // orderService.updateSellerOrderBeforePay(orders.get(0).getBatchSn());

    Map<String, Object> map = new HashMap<String, Object>();

    map.put("orderSn", orders.get(0).getBatchSn());
    response.setMsg(map);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 删除购物车中录单记录
   * 
   * @param request
   * @return
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody BaseResponse delete(@RequestBody BaseRequest request) {
    BaseResponse response = new BaseResponse();

    Long userId = request.getUserId();
    String token = request.getToken();

    List<Long> deleteList = request.getEntityIds();

    Long deleteLong[] = new Long[deleteList.size()];
    deleteLong = deleteList.toArray(deleteLong);
    sellerOrderCartService.delete(deleteLong);

    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }
}

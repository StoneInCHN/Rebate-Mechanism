package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.Order;
import org.rebate.entity.commonenum.CommonEnum.OrderStatus;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.request.OrderReq;
import org.rebate.service.OrderService;
import org.rebate.utils.ExportUtils;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("orderController")
@RequestMapping("/console/order")
public class OrderController extends BaseController {

  @Resource(name = "orderServiceImpl")
  private OrderService orderService;


  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, OrderReq request, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (StringUtils.isNotEmpty(request.getSn())) {
      filters.add(Filter.like("sn", "%" + request.getSn() + "%"));
      model.addAttribute("sn", request.getSn());
    }
    if (StringUtils.isNotEmpty(request.getCellPhoneNum())) {
      filters.add(Filter.like("endUser&cellPhoneNum", "%" + request.getCellPhoneNum() + "%"));
      model.addAttribute("cellPhoneNum", request.getCellPhoneNum());
    }
    if (StringUtils.isNotEmpty(request.getEndUserNickName())) {
      filters.add(Filter.like("endUser&nickName", "%" + request.getEndUserNickName() + "%"));
      model.addAttribute("endUserNickName", request.getEndUserNickName());
    }
    if (StringUtils.isNotEmpty(request.getSellerName())) {
      filters.add(Filter.like("seller&name", "%" + request.getSellerName() + "%"));
      model.addAttribute("sellerName", request.getSellerName());
    }
    if (StringUtils.isNotEmpty(request.getPaymentType())) {
      filters.add(Filter.eq("paymentTypeId", request.getPaymentType()));
      model.addAttribute("paymentType", request.getPaymentType());
    }
    if (request.getOrderStatus() != null) {
      if (OrderStatus.PAID == request.getOrderStatus()) {
        OrderStatus[] statusArray = {OrderStatus.PAID, OrderStatus.FINISHED};
        filters.add(Filter.in("status", statusArray));
      } else {
        filters.add(Filter.eq("status", request.getOrderStatus()));
      }
      model.addAttribute("orderStatus", request.getOrderStatus());
    }
    if (request.getPaymentChannel() != null) {
      filters.add(Filter.eq("paymentChannel", request.getPaymentChannel()));
      model.addAttribute("paymentChannel", request.getPaymentChannel());
    }
    if (request.getOrderDateFrom() != null) {
      filters.add(Filter.ge("createDate", TimeUtils.formatDate2Day(request.getOrderDateFrom())));
      model.addAttribute("orderDateFrom", request.getOrderDateFrom());
    }
    if (request.getOrderDateTo() != null) {
      filters.add(Filter.lt("createDate",
          TimeUtils.addDays(1, TimeUtils.formatDate2Day(request.getOrderDateTo()))));
      model.addAttribute("orderDateTo", request.getOrderDateTo());
    }
    filters.add(Filter.eq("isSallerOrder", false));
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("page", orderService.findPage(pageable));
    return "/order/list";
  }

  /**
   * 查看
   */
  @RequestMapping(value = "/details", method = RequestMethod.GET)
  public String details(Long id, ModelMap model) {
    Order order = orderService.find(id);
    model.addAttribute("order", order);
    return "/order/details";
  }

  /**
   * 数据导出
   */
  @RequestMapping(value = "/dataExport", method = {RequestMethod.GET, RequestMethod.POST})
  public void dataExport(OrderReq request, HttpServletResponse response) {
    List<Filter> filters = new ArrayList<Filter>();
    filters.add(Filter.eq("isSallerOrder", false));// false为订单，true为录单
    if (StringUtils.isNotEmpty(request.getSn())) {
      filters.add(Filter.like("sn", "%" + request.getSn() + "%"));
    }
    if (StringUtils.isNotEmpty(request.getCellPhoneNum())) {
      filters.add(Filter.like("endUser&cellPhoneNum", "%" + request.getCellPhoneNum() + "%"));
    }
    if (StringUtils.isNotEmpty(request.getEndUserNickName())) {
      filters.add(Filter.like("endUser&nickName", "%" + request.getEndUserNickName() + "%"));
    }
    if (StringUtils.isNotEmpty(request.getSellerName())) {
      filters.add(Filter.like("seller&name", "%" + request.getSellerName() + "%"));
    }
    if (StringUtils.isNotEmpty(request.getPaymentType())) {
      filters.add(Filter.like("paymentType", "%" + request.getPaymentType() + "%"));
    }
    if (request.getOrderStatus() != null) {
      filters.add(Filter.eq("status", request.getOrderStatus()));
    }
    if (request.getOrderDateFrom() != null) {
      filters.add(Filter.ge("createDate", TimeUtils.formatDate2Day(request.getOrderDateFrom())));
    }
    if (request.getOrderDateTo() != null) {
      filters.add(Filter.lt("createDate",
          TimeUtils.addDays(1, TimeUtils.formatDate2Day(request.getOrderDateTo()))));
    }
    List<Order> lists = orderService.findList(null, filters, null);
    if (lists != null && lists.size() > 0) {
      String title = "Order List"; // 工作簿标题，同时也是excel文件名前缀
      String[] headers =
          {"sn", "endUserPhone", "endUserName", "sellerID", "sellerName", "sellerCategory",
              "sellerDiscount", "createDate", "amount", "paymentType", "paymentTime", "status",
              "sellerIncome", "rebateAmount", "userScore", "sellerScore", "isClearing"}; // 需要导出的字段
      String[] headersName =
          {"订单编号", "用户手机号", "用户昵称", "店铺编号", "店铺名", "店铺类别", "店铺折扣", "下单时间", "消费金额", "支付方式", "支付时间",
              "订单状态", "商家直接收益", "返利金额", "用户积分返利", "商家积分返利", "是否结算(提现)"}; // 字段对应列的列名
      List<Map<String, String>> mapList = ExportUtils.prepareExportOrder(lists);
      if (mapList.size() > 0) {
        exportListToExcel(response, mapList, title, headers, headersName);
      }
    }
  }

}

package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.EndUser;
import org.rebate.entity.Order;
import org.rebate.entity.commonenum.CommonEnum.OrderStatus;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.request.OrderReq;
import org.rebate.service.OrderService;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller("sallerOrderController")
@RequestMapping("/console/sallerOrder")
public class SallerOrderController extends BaseController{
  
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
    if (request.getOrderStatus() != null) {
      filters.add(Filter.eq("status", request.getOrderStatus()));
      model.addAttribute("orderStatus", request.getOrderStatus());
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
    filters.add(Filter.eq("isSallerOrder", true));
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("page", orderService.findPage(pageable));
    return "/sallerOrder/list";
  }

  /**
   * 查看
   */
  @RequestMapping(value = "/details", method = RequestMethod.GET)
  public String details(Long id, ModelMap model) {
    Order order = orderService.find(id);
    model.addAttribute("order", order);
    return "/sallerOrder/details";
  }
  
  /**
   * 设置业务员
   */
  @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
  public @ResponseBody Message updateStatus(Long id ,OrderStatus status) {
     if(id==null||status==null){
       return ERROR_MESSAGE;
     }
    Order order = orderService.find(id);
    order.setStatus(status);
    orderService.update(order);
    return SUCCESS_MESSAGE;
  }
}

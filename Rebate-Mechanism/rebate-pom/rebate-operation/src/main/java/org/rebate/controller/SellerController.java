package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.Area;
import org.rebate.entity.Seller;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.request.SellerRequest;
import org.rebate.service.AreaService;
import org.rebate.service.SellerCategoryService;
import org.rebate.service.SellerService;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("sellerController")
@RequestMapping("/console/seller")
public class SellerController extends BaseController {

  @Resource(name = "sellerServiceImpl")
  private SellerService sellerService;

  @Resource(name = "sellerCategoryServiceImpl")
  private SellerCategoryService sellerCategoryService;

  @Resource(name = "areaServiceImpl")
  private AreaService areaService;
  
  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, SellerRequest request, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (StringUtils.isNotEmpty(request.getName())) {
      filters.add(Filter.like("name", request.getName()));
      model.addAttribute("name", request.getName());
    }
    if (StringUtils.isNotEmpty(request.getContactCellPhone())) {
      filters.add(Filter.like("contactCellPhone", request.getContactCellPhone()));
      model.addAttribute("contactCellPhone", request.getContactCellPhone());
    }
    if (StringUtils.isNotEmpty(request.getCellPhoneNum())) {
      filters.add(Filter.like("endUser&cellPhoneNum", "%" + request.getCellPhoneNum() + "%"));
      model.addAttribute("cellPhoneNum", request.getCellPhoneNum());
    }
    if (StringUtils.isNotEmpty(request.getContactPerson())) {
      filters.add(Filter.like("contactPerson", request.getContactPerson()));
      model.addAttribute("contactPerson", request.getContactPerson());
    }
    if (request.getAreaId() != null) {
      filters.add(Filter.eq("area", request.getAreaId()));
      model.addAttribute("areaId", request.getAreaId());
    }
    if (request.getSellerCategoryId() != null) {
      filters.add(Filter.eq("sellerCategory", request.getSellerCategoryId()));
      model.addAttribute("sellerCategoryId", request.getSellerCategoryId());
    }
    if (request.getAccountStatus() != null) {
      filters.add(Filter.eq("accountStatus", request.getAccountStatus()));
      model.addAttribute("accountStatus", request.getAccountStatus());
    }
    if (request.getApplyFromDate() != null) {
      filters.add(Filter.ge("createDate", TimeUtils.formatDate2Day(request.getApplyFromDate())));
      model.addAttribute("applyFromDate", request.getApplyFromDate());
    }
    if (request.getApplyToDate() != null) {
      filters.add(Filter.le("createDate",
          TimeUtils.addDays(1, TimeUtils.formatDate2Day(request.getApplyToDate()))));
      model.addAttribute("applyToDate", request.getApplyToDate());
    }
    filters.add(Filter.ne("accountStatus", AccountStatus.DELETE));
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("sellerCategorys", sellerCategoryService.findAll());
    model.addAttribute("page", sellerService.findPage(pageable));
    return "/seller/list";
  }


  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String update(Seller seller,Long areaId) {
    Seller temp = sellerService.find(seller.getId());
    temp.setAccountStatus(seller.getAccountStatus());
    temp.setAddress(seller.getAddress());
    if(areaId!=null){
      Area area = areaService.find(areaId);
      if(area!=null){
        temp.setArea(area);
      }
    }
    temp.setAvgPrice(seller.getAvgPrice());
    temp.setDescription(seller.getDescription());
    temp.setDiscount(seller.getDiscount());
    temp.setLongitude(seller.getLongitude());
    temp.setLatitude(seller.getLatitude());
    temp.setBusinessTime(seller.getBusinessTime());
    temp.setRemark(seller.getRemark());
    temp.setLimitAmountByDay(seller.getLimitAmountByDay());
    sellerService.update(temp);
    return "redirect:list.jhtml";
  }


  /**
   * 查看
   */
  @RequestMapping(value = "/details", method = RequestMethod.GET)
  public String details(Long id, ModelMap model) {
    Seller seller = sellerService.find(id);
    model.addAttribute("seller", seller);
    model.addAttribute("envImages", seller.getEnvImages());
    return "/seller/details";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    List<Seller> sellers = new ArrayList<Seller>();
    if (ids != null && ids.length > 0) {
      for (Long id : ids) {
        Seller seller = sellerService.find(id);
        seller.setAccountStatus(AccountStatus.DELETE);
        sellers.add(seller);
      }
    }
    sellerService.update(sellers);
    return SUCCESS_MESSAGE;
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("seller", sellerService.find(id));
    return "/seller/edit";
  }


  /**
   * 禁用
   */
  @RequestMapping(value = "/locked", method = RequestMethod.POST)
  public @ResponseBody Message locked(Long[] ids) {
    List<Seller> sellers = new ArrayList<Seller>();
    if (ids != null && ids.length > 0) {
      for (Long id : ids) {
        Seller seller = sellerService.find(id);
        seller.setAccountStatus(AccountStatus.LOCKED);
        sellers.add(seller);
      }
    }
    sellerService.update(sellers);
    return SUCCESS_MESSAGE;
  }

  /**
   * 查看商家在百度地图中的位置点
   */
  @RequestMapping(value = "/viewPosition", method = RequestMethod.GET)
  public String viewPosition(Long id, ModelMap model) {
    Seller seller = sellerService.find(id);
    model.addAttribute("seller", seller);
    return "/seller/viewPosition";
  }

  /**
   * 编辑商家在百度地图中的位置点
   */
  @RequestMapping(value = "/editPosition", method = RequestMethod.GET)
  public String editPosition(Long id, ModelMap model) {
    Seller seller = sellerService.find(id);
    model.addAttribute("seller", seller);
    return "/seller/editPosition";
  }
}

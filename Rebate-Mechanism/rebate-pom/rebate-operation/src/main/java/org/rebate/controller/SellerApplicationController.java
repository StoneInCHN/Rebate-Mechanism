package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.EndUser;
import org.rebate.entity.SalesmanSellerRelation;
import org.rebate.entity.SellerApplication;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.request.SellerApplicationReq;
import org.rebate.service.SalesmanSellerRelationService;
import org.rebate.service.SellerApplicationService;
import org.rebate.service.SellerCategoryService;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller("sellerApplicationController")
@RequestMapping("/console/sellerApply")
public class SellerApplicationController extends BaseController {

  @Resource(name = "sellerApplicationServiceImpl")
  private SellerApplicationService sellerApplicationService;

  @Resource(name = "sellerCategoryServiceImpl")
  private SellerCategoryService sellerCategoryService;
  
  @Resource(name="salesmanSellerRelationServiceImpl")
  private SalesmanSellerRelationService salesmanSellerRelationService;

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    SellerApplication sellerApplication = sellerApplicationService.find(id);
    model.addAttribute("sellerApply", sellerApplication);
    model.addAttribute("envImages", sellerApplication.getEnvImages());
    List<Filter> filters = new ArrayList<Filter>();
    filters.add(Filter.eq("sellerApplication", sellerApplication.getId()));
    List<SalesmanSellerRelation> salesmanSellerRelations = salesmanSellerRelationService.findList(null, null, filters, null);
    if(salesmanSellerRelations!=null && salesmanSellerRelations.size() == 1){
      SalesmanSellerRelation salesmanSellerRelation = salesmanSellerRelations.get(0);
      EndUser salesMan = salesmanSellerRelation.getEndUser();
      model.addAttribute("salesMan", salesMan);
    }
    
    return "/sellerApply/edit";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public @ResponseBody Message update(SellerApplication sellerApply) {
    return sellerApplicationService.applyUpdate(sellerApply);
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, SellerApplicationReq request, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (StringUtils.isNotEmpty(request.getSellerName())) {
      filters.add(Filter.like("sellerName", request.getSellerName()));
      model.addAttribute("sellerName", request.getSellerName());
    }
    if (StringUtils.isNotEmpty(request.getLicenseNum())) {
      filters.add(Filter.like("licenseNum", request.getLicenseNum()));
      model.addAttribute("licenseNum", request.getLicenseNum());
    }
    if (StringUtils.isNotEmpty(request.getSellerName())) {
      filters.add(Filter.like("sellerName", request.getSellerName()));
      model.addAttribute("sellerName", request.getSellerName());
    }
    if (StringUtils.isNotEmpty(request.getContactCellPhone())) {
      filters.add(Filter.like("contactCellPhone", request.getContactCellPhone()));
      model.addAttribute("contactCellPhone", request.getContactCellPhone());
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
    if (request.getApplyStatus() != null) {
      filters.add(Filter.eq("applyStatus", request.getApplyStatus()));
      model.addAttribute("applyStatus", request.getApplyStatus());
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
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("sellerCategorys", sellerCategoryService.findAll());
    model.addAttribute("page", sellerApplicationService.findPage(pageable));
    return "/sellerApply/list";
  }

  /**
   * 查看
   */
  @RequestMapping(value = "/details", method = RequestMethod.GET)
  public String details(Long id, ModelMap model) {
    SellerApplication sellerApplication = sellerApplicationService.find(id);
    model.addAttribute("sellerApply", sellerApplication);
    model.addAttribute("envImages", sellerApplication.getEnvImages());
    List<Filter> filters = new ArrayList<Filter>();
    filters.add(Filter.eq("sellerApplication", sellerApplication.getId()));
    List<SalesmanSellerRelation> salesmanSellerRelations = salesmanSellerRelationService.findList(null, null, filters, null);
    if(salesmanSellerRelations!=null && salesmanSellerRelations.size() == 1){
      SalesmanSellerRelation salesmanSellerRelation = salesmanSellerRelations.get(0);
      EndUser salesMan = salesmanSellerRelation.getEndUser();
      model.addAttribute("salesMan", salesMan);
    }
    return "/sellerApply/details";
  }

  /**
   * 查看
   */
  @RequestMapping(value = "/viewPosition", method = RequestMethod.GET)
  public String viewPosition(Long id, ModelMap model) {
    SellerApplication sellerApplication = sellerApplicationService.find(id);
    model.addAttribute("sellerApply", sellerApplication);
    return "/sellerApply/viewPosition";
  }

}

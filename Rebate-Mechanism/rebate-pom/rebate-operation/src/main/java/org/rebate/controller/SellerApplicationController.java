package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.EndUser;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerApplication;
import org.rebate.entity.SellerCategory;
import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.service.EndUserService;
import org.rebate.service.SellerApplicationService;
import org.rebate.service.SellerCategoryService;
import org.rebate.service.SellerService;
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
  
  @Resource(name = "sellerServiceImpl")
  private SellerService sellerService;
  
  @Resource(name = "sellerCategoryServiceImpl")
  private SellerCategoryService sellerCategoryService;

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;
  
  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("sellerApply", sellerApplicationService.find(id));
    return "/sellerApply/edit";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public @ResponseBody Message update(SellerApplication sellerApply) {
    SellerApplication apply = sellerApplicationService.find(sellerApply.getId());
    try {
      apply.setApplyStatus(sellerApply.getApplyStatus());
      if (ApplyStatus.AUDIT_PASSED == sellerApply.getApplyStatus()) {
        Seller seller =new Seller();
        SellerCategory sellerCategory = apply.getSellerCategory();
        EndUser endUser = null;
        if(apply.getId()!=null){
          endUser= endUserService.find(apply.getId()); 
          if(endUser!=null){
            seller.setEndUser(endUser);
          }
        }
        seller.setAddress(apply.getAddress());
        seller.setSellerCategory(sellerCategory);
        seller.setArea(apply.getArea());
        seller.setContactCellPhone(apply.getContactCellPhone());
        seller.setContactPerson(apply.getContactPerson());
        seller.setLatitude(apply.getLatitude());
        seller.setLicense(apply.getLicense());
        seller.setLongitude(apply.getLongitude());
        seller.setStorePhone(apply.getStorePhone());
        seller.setStorePictureUrl(apply.getStorePhoto());
        seller.setName(apply.getSellerName());
        sellerService.save(seller);
      }
      sellerApplicationService.update(apply);
      return SUCCESS_MESSAGE;
    } catch (Exception e) {
      return ERROR_MESSAGE;
    }
   
    
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model) {
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("page", sellerApplicationService.findPage(pageable));
    return "/sellerApply/list";
  }

  /**
   * 查看
   */
  @RequestMapping(value = "/details", method = RequestMethod.GET)
  public String details(Long id, ModelMap model) {
    model.addAttribute("apply", sellerApplicationService.find(id));
    return "/sellerApply/details";
  }

}

package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.BankCard;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.paging.Pageable;
import org.rebate.request.BankCardRequest;
import org.rebate.service.BankCardService;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("console/bankCard")
@Controller("bankCardController")
public class BankCardController extends BaseController{

  @Resource(name="bankCardServiceImpl")
  private BankCardService bankCardService;
  
  
  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, BankCardRequest request, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (StringUtils.isNotEmpty(request.getOwnerName())) {
      filters.add(Filter.like("ownerName", "%" + request.getOwnerName() + "%"));
      model.addAttribute("ownerName", request.getOwnerName());
    }
    if (StringUtils.isNotEmpty(request.getCardNum())) {
      filters.add(Filter.like("cardNum", "%" + request.getCardNum() + "%"));
      model.addAttribute("cardNum", request.getCardNum());
    }
    if (StringUtils.isNotEmpty(request.getReservedMobile())) {
      filters.add(Filter.like("reservedMobile", "%" + request.getReservedMobile() + "%"));
      model.addAttribute("reservedMobile", request.getReservedMobile());
    }
    if (StringUtils.isNotEmpty(request.getIdCard())) {
      filters.add(Filter.like("idCard", "%" + request.getIdCard() + "%"));
      model.addAttribute("idCard", request.getIdCard());
    }
    if (StringUtils.isNotEmpty(request.getNickName())) {
      filters.add(Filter.like("endUser&nickName", "%" + request.getNickName() + "%"));
      model.addAttribute("nickName", request.getNickName());
    }
    if (StringUtils.isNotEmpty(request.getCellPhoneNum())) {
      filters.add(Filter.like("endUser&cellPhoneNum", "%" + request.getCellPhoneNum() + "%"));
      model.addAttribute("cellPhoneNum", request.getCellPhoneNum());
    }
    if (request.getDelStatus()!= null) {
      filters.add(Filter.eq("delStatus", request.getDelStatus()));
      model.addAttribute("delStatus", request.getDelStatus());
    }
    if (request.getIsDefault()!= null) {
      filters.add(Filter.eq("isDefault", request.getIsDefault()));
      model.addAttribute("isDefault", request.getIsDefault());
    }
    if (request.getDateFrom() != null) {
      filters.add(Filter.ge("createDate", TimeUtils.formatDate2Day(request.getDateFrom())));
      model.addAttribute("dateFrom", request.getDateFrom());
    }
    if (request.getDateTo() != null) {
      filters.add(Filter.lt("createDate",
          TimeUtils.addDays(1, TimeUtils.formatDate2Day(request.getDateTo()))));
      model.addAttribute("dateTo", request.getDateTo());
    }
    
    pageable.setFilters(filters);
    model.addAttribute("page", bankCardService.findPage(pageable));
    return "/bankCard/list";
  }
  
  /**
   * 查看
   */
  @RequestMapping(value = "/details", method = RequestMethod.GET)
  public String details(Long id, ModelMap model) {
    BankCard bankCard = bankCardService.find(id);
    model.addAttribute("bankCard", bankCard);
    return "/bankCard/details";
  }
  
}

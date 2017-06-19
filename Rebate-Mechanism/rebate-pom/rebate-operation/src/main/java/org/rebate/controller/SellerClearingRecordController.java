package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.BankCard;
import org.rebate.entity.SellerClearingRecord;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.request.SellerClearingRecordReq;
import org.rebate.service.SellerClearingRecordService;
import org.rebate.service.BankCardService;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("sellerClearingRecordController")
@RequestMapping("/console/sellerClearingRecord")
public class SellerClearingRecordController extends BaseController {

  @Resource(name = "sellerClearingRecordServiceImpl")
  private SellerClearingRecordService sellerClearingRecordService;
  
  @Resource(name = "bankCardServiceImpl")
  private BankCardService bankCardService;

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, SellerClearingRecordReq request, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (StringUtils.isNotEmpty(request.getSn())) {
      filters.add(Filter.like("sn", "%" + request.getSn() + "%"));
      model.addAttribute("sn", request.getSn());
    }
    if (StringUtils.isNotEmpty(request.getClearingSn())) {
      filters.add(Filter.like("clearingSn", "%" + request.getClearingSn() + "%"));
      model.addAttribute("clearingSn", request.getClearingSn());
    }
    if (StringUtils.isNotEmpty(request.getReqSn())) {
      filters.add(Filter.like("reqSn", "%" + request.getReqSn() + "%"));
      model.addAttribute("reqSn", request.getReqSn());
    }
    if (StringUtils.isNotEmpty(request.getEndUserCellPhone())) {
      filters.add(Filter.like("endUser&cellPhoneNum", "%" + request.getEndUserCellPhone() + "%"));
      model.addAttribute("endUserCellPhone", request.getEndUserCellPhone());
    }
    if (StringUtils.isNotEmpty(request.getSellerName())) {
      filters.add(Filter.like("seller&name", "%" + request.getSellerName() + "%"));
      model.addAttribute("sellerName", request.getSellerName());
    }
    if (request.getClearingStatus() != null) {
      filters.add(Filter.eq("clearingStatus", request.getClearingStatus()));
      model.addAttribute("clearingStatus", request.getClearingStatus());
    }
    if (request.getValid() != null) {
        filters.add(Filter.eq("valid", request.getValid()));
        model.addAttribute("valid", request.getValid());
      }
    if (request.getDateFrom() != null) {
      filters.add(Filter.ge("createDate", TimeUtils.formatDate2Day(request.getDateFrom())));
      model.addAttribute("orderDateFrom", request.getDateFrom());
    }
    if (request.getDateTo() != null) {
      filters.add(Filter.lt("createDate",
          TimeUtils.addDays(1, TimeUtils.formatDate2Day(request.getDateTo()))));
      model.addAttribute("orderDateTo", request.getDateTo());
    }
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("page", sellerClearingRecordService.findPage(pageable));
    return "/sellerClearingRecord/list";
  }

  /**
   * 查看
   */
  @RequestMapping(value = "/details", method = RequestMethod.GET)
  public String details(Long id, ModelMap model) {
    SellerClearingRecord sellerClearingRecord = sellerClearingRecordService.find(id);
    model.addAttribute("sellerClearingRecord", sellerClearingRecord);
    return "/sellerClearingRecord/details";
  }

  /**
   * 单笔实时结算
   */
  @RequestMapping(value = "/singlePay", method = RequestMethod.POST)
  public @ResponseBody Message singlePay(Long id) {
	 SellerClearingRecord sellerClearingRecord = null;
     if(id == null){
       return ERROR_MESSAGE;
     }
     sellerClearingRecord = sellerClearingRecordService.find(id);
	 if (sellerClearingRecord == null) {
		 return ERROR_MESSAGE;
	 }
	 BankCard bankCard = null;
	 Long bankCardId = sellerClearingRecord.getBankCardId();
	 if (bankCardId == null) {
	   if (sellerClearingRecord.getEndUser() != null) {
	     bankCard = bankCardService.getDefaultCard(sellerClearingRecord.getEndUser());
       }else if(sellerClearingRecord.getSeller() != null 
           && sellerClearingRecord.getSeller().getEndUser() != null){
         bankCard = bankCardService.getDefaultCard(sellerClearingRecord.getSeller().getEndUser());
         sellerClearingRecord.setEndUser(sellerClearingRecord.getSeller().getEndUser());
       }
     }else {
       bankCard = bankCardService.find(bankCardId);
     }
	 if (bankCard == null || bankCard.getBankName() == null || bankCard.getCardNum() == null) {
		 return Message.error("无效的银行卡");
	 }else {
	    sellerClearingRecord.setBankCardId(bankCard.getId());
	    sellerClearingRecordService.update(sellerClearingRecord);
     }	 
     return sellerClearingRecordService.singlePay(sellerClearingRecord, bankCard);
  }
}

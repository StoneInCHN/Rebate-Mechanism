package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.BankCard;
import org.rebate.entity.ClearingOrderRelation;
import org.rebate.entity.SellerClearingRecord;
import org.rebate.entity.commonenum.CommonEnum.PaymentChannel;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.json.beans.SellerClearingResult;
import org.rebate.request.SellerClearingRecordReq;
import org.rebate.service.BankCardService;
import org.rebate.service.SellerClearingRecordService;
import org.rebate.utils.CommonUtils;
import org.rebate.utils.ExportUtils;
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
    if (request.getPaymentChannel() != null) {
        filters.add(Filter.eq("paymentChannel", request.getPaymentChannel()));
        model.addAttribute("paymentChannel", request.getPaymentChannel());
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
    
    List<ClearingOrderRelation> relations = sellerClearingRecordService.getRelationListByRecordId(id);
    model.addAttribute("relations", relations);
    
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
	 //获取支付渠道
	 PaymentChannel channel = CommonUtils.getPaymentChannel();

	 //1. 通联支付渠道
	 if (PaymentChannel.ALLINPAY == channel) {
		 return sellerClearingRecordService.singlePayByAllinpay(sellerClearingRecord, bankCard);
	 }
	 //2. 九派支付渠道 
	 else if(PaymentChannel.JIUPAI == channel) {
		 return sellerClearingRecordService.singlePayByJiuPai(sellerClearingRecord, bankCard);
	 }	 
	 return ERROR_MESSAGE;
  }
  /**
   * 数据导出
   */
  @RequestMapping(value = "/dataExport", method = {RequestMethod.GET,RequestMethod.POST})
  public void dataExport(HttpServletResponse response) {   
    List<SellerClearingResult> lists = sellerClearingRecordService.findClearingResult();
    if (lists != null && lists.size() > 0) {
      String title = "Seller Clearing Record List"; // 工作簿标题，同时也是excel文件名前缀
      String[] headers = {"paymentDate", "sellerName","amount","sellerIncome", "profit", "count"}; // 需要导出的字段
      String[] headersName = {"交易日期", "商户名称","交易额","商家结算金额", "毛利润", "交易笔数"}; // 字段对应列的列名
      List<Map<String, String>> mapList = ExportUtils.prepareExportSellerClearing(lists);
      if (mapList.size() > 0) {
        exportListToExcel(response, mapList, title, headers, headersName);
      }
    }
  }
}

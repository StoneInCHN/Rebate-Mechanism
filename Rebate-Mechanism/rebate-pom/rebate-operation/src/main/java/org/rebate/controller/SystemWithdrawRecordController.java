package org.rebate.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.BankCard;
import org.rebate.entity.SystemWithdrawRecord;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
//import org.rebate.job.SystemWithdrawRecordJob;
import org.rebate.request.SystemWithdrawRecordReq;
import org.rebate.service.AdminService;
import org.rebate.service.BankCardService;
import org.rebate.service.SystemWithdrawRecordService;
import org.rebate.utils.ToolsUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("systemWithdrawRecordController")
@RequestMapping("/console/systemWithdrawRecord")
public class SystemWithdrawRecordController extends BaseController {

  @Resource(name = "systemWithdrawRecordServiceImpl")
  private SystemWithdrawRecordService systemWithdrawRecordService;
  
//  @Resource(name = "leScoreRecordJob")
//  private SystemWithdrawRecordJob leScoreRecordJob;
  
  @Resource(name = "bankCardServiceImpl")
  private BankCardService bankCardService;

  @Resource(name = "adminServiceImpl")
  private AdminService adminService;
  
  

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, SystemWithdrawRecordReq req, ModelMap model) {
    
    List<Filter> filters = new ArrayList<Filter>();
    //提现人
    if (req.getOperator() != null) {
      filters.add(Filter.like("operator", "%" + req.getOperator() + "%"));
    }
    //提现人手机号
    if (req.getCellPhoneNum() != null) {
      filters.add(Filter.like("cellPhoneNum", "%" + req.getCellPhoneNum() + "%"));
    }
    //提现状态
    if (req.getStatus() != null) {
      filters.add(Filter.eq("status", req.getStatus()));
    }
    //银行卡号
    if (req.getCardNum() != null) {
      filters.add(Filter.eq("cardNum", req.getCardNum()));
    }
    if (req.getBeginDate() != null) {
      Filter dateGeFilter = new Filter("createDate", Operator.ge, req.getBeginDate());
      filters.add(dateGeFilter);
    }
    if (req.getEndDate() != null) {
      Filter dateLeFilter = new Filter("createDate", Operator.le, req.getEndDate());
      filters.add(dateLeFilter);
    }
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("page", systemWithdrawRecordService.findPage(pageable));
    model.addAttribute("operator", req.getOperator());
    model.addAttribute("cellPhoneNum", req.getCellPhoneNum());
    model.addAttribute("status", req.getStatus());
    model.addAttribute("cardNum", req.getCardNum());
    model.addAttribute("beginDate", req.getBeginDate());
    model.addAttribute("endDate", req.getEndDate());
    return "/systemWithdrawRecord/list";
  }


  /**
   * 查看
   */
  @RequestMapping(value = "/details", method = RequestMethod.GET)
  public String details(Long id, ModelMap model) {
    SystemWithdrawRecord leScoreRecord = systemWithdrawRecordService.find(id);
    model.addAttribute("record", leScoreRecord);
    return "/systemWithdrawRecord/details";
  }
  /**
   * 添加验证
   */
  @RequestMapping(value = "/validation", method = RequestMethod.GET)
  public String validation(ModelMap model) {
    model.addAttribute("admin",adminService.getCurrent());
    return "/systemWithdrawRecord/validation";
  }  
  /**
   * 请求验证码
   */
  @RequestMapping(value = "/reqeustSmsCode", method = RequestMethod.GET)
  public void reqeustSmsCode(ModelMap model) {

  } 
  
  /**
   * 单笔实时提现
   */
  @RequestMapping(value = "/singlePay", method = RequestMethod.POST)
  public @ResponseBody Message singlePay(Long id) {
	 SystemWithdrawRecord record = null;
     if(id == null){
       return ERROR_MESSAGE;
     }
     record = systemWithdrawRecordService.find(id);
	 if (record == null) {
		 return ERROR_MESSAGE;
	 }
	 BankCard bankCard = bankCardService.find(record.getBankCardId());
	 if (bankCard == null || bankCard.getBankName() == null || bankCard.getCardNum() == null) {
		 return Message.error("无效的银行卡");
	 }	 
     //return systemWithdrawRecordService.singlePay(record, bankCard);
	 return null;
  }
}

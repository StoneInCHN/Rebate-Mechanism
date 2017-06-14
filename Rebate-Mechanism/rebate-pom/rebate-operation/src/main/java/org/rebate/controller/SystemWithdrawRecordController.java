package org.rebate.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.rebate.beans.Message;
import org.rebate.beans.SMSVerificationCode;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.Admin;
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
import org.rebate.utils.LogUtil;
import org.rebate.utils.TokenGenerator;
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
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String validationPage(ModelMap model) {
	Admin admin = adminService.getCurrent();
	if (!adminService.isSystemAdmin(admin)) {//非内置账户admin
		return "redirect:list.jhtml";
	}
	model.addAttribute("admin",admin);
	return "/systemWithdrawRecord/validation";
  }  
  /**
   * 请求验证码
   */
  @RequestMapping(value = "/reqeustSmsCode", method = RequestMethod.POST)
  public @ResponseBody Message reqeustSmsCode() {
	Admin admin = adminService.getCurrent();
	if (!adminService.isSystemAdmin(admin)) {//非内置账户admin
		LogUtil.debug(this.getClass(), "reqeustSmsCode", "非系统内置账户(非admin管理员)");
	}else if (admin.getCellPhoneNum() == null) {
		LogUtil.debug(this.getClass(), "reqeustSmsCode", "admin管理员未配置预留手机号");
	}else if (isMobileNumber(admin.getCellPhoneNum())) {
		String cellPhoneNum = admin.getCellPhoneNum();
	    SMSVerificationCode smsVerificationCode = adminService.getSmsCode(cellPhoneNum);
	    if (smsVerificationCode != null) {
	    	  adminService.deleteSmsCode(cellPhoneNum);
	    }
	    Integer smsCode = (int) ((Math.random() * 9 + 1) * 1000);
	    ToolsUtils.sendSmsMsg(cellPhoneNum, message("rebate.admin.smsCodeContent", smsCode.toString()));// 发送短信验证码
	    SMSVerificationCode newSmsCode = new SMSVerificationCode();
	    newSmsCode.setCellPhoneNum(cellPhoneNum);
	    newSmsCode.setSmsCode(smsCode.toString());
	    newSmsCode.setTimeoutToken(new Long(new Date().getTime()).toString());
	    try {
	      adminService.createSmsCode(cellPhoneNum, newSmsCode);
	      LogUtil.debug(this.getClass(), "reqeustSmsCode", "Send SmsCode for cellPhone number: %s, smsCode: %s", cellPhoneNum, smsCode.toString());
	      return SUCCESS_MESSAGE;
        } catch (Exception e) {
          e.printStackTrace();
          LogUtil.debug(this.getClass(), "reqeustSmsCode", "Catch Exception:", e.getMessage());
          return Message.error(e.getMessage());
        }
	}
	return ERROR_MESSAGE;
  } 
  /**
   * 验证admin密码和手机验证码
   */
  @RequestMapping(value = "/validationPwdSms", method = RequestMethod.POST)
  public String validationPwdSms(String password, String smsCode, ModelMap model) {
	Admin admin = adminService.getCurrent();
	if (password != null && smsCode != null && DigestUtils.md5Hex(password).equals(admin.getPassword())) {//密码有效
	    SMSVerificationCode smsVerficationCode = adminService.getSmsCode(admin.getCellPhoneNum());
	    if (smsVerficationCode != null) {
	    	String code = smsVerficationCode.getSmsCode();
	    	String timeoutToken = smsVerficationCode.getTimeoutToken();
	    	if (timeoutToken != null && !TokenGenerator.smsCodeTokenTimeOut(timeoutToken, setting.getSmsCodeTimeOut())) {
	    		if (smsCode.equals(code)) {//验证码有效
	    			adminService.deleteSmsCode(admin.getCellPhoneNum());
	    			model.addAttribute("admin",adminService.getCurrent());
	    			BankCard defaultCard = bankCardService.getDefaultCard(admin);
	                if (defaultCard != null) {
	                	model.addAttribute("defaultCard", defaultCard);
	                }
	    			return "/systemWithdrawRecord/withdraw";
	    		}
	    	}
	    }
	}
    return "redirect:add.jhtml";
  }  
  /**
   * 平台单笔实时提现
   */
  @RequestMapping(value = "/singlePay", method = RequestMethod.POST)
  public @ResponseBody Message singlePay(BigDecimal amount) {
     if(amount == null){
       return Message.error("提现金额无效");
     }
     Admin admin = adminService.getCurrent();
     BankCard bankCard = bankCardService.getDefaultCard(admin);
	 if (bankCard == null || bankCard.getBankName() == null || bankCard.getCardNum() == null) {
		 return Message.error("无效的银行卡");
	 }	 
     return systemWithdrawRecordService.singlePay(admin, amount, bankCard);
  }
}

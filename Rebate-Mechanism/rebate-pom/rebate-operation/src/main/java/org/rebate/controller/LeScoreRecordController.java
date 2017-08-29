package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.commonenum.CommonEnum.LeScoreType;
import org.rebate.entity.commonenum.CommonEnum.PaymentChannel;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.job.LeScoreRecordJob;
import org.rebate.request.LeScoreRecordReq;
import org.rebate.service.BankCardService;
import org.rebate.service.LeScoreRecordService;
import org.rebate.utils.TimeUtils;
import org.rebate.utils.CommonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("leScoreRecordController")
@RequestMapping("/console/leScoreRecord")
public class LeScoreRecordController extends BaseController {

  @Resource(name = "leScoreRecordServiceImpl")
  private LeScoreRecordService leScoreRecordService;

  @Resource(name = "leScoreRecordJob")
  private LeScoreRecordJob leScoreRecordJob;

  @Resource(name = "bankCardServiceImpl")
  private BankCardService bankCardService;


  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, LeScoreRecordReq req, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    filters.add(Filter.eq("leScoreType", LeScoreType.WITHDRAW));
    if (req.getUserName() != null) {
      filters.add(Filter.like("endUser&nickName", "%" + req.getUserName() + "%"));
      model.addAttribute("userName", req.getUserName());
    }
    if (req.getCellPhoneNum() != null) {
      filters.add(Filter.like("endUser&cellPhoneNum", "%" + req.getCellPhoneNum() + "%"));
      model.addAttribute("cellPhoneNum", req.getCellPhoneNum());
    }
    if (req.getWithdrawStatus() != null) {
      filters.add(Filter.eq("withdrawStatus", req.getWithdrawStatus()));
      model.addAttribute("withdrawStatus", req.getWithdrawStatus());
    }
    if (req.getPaymentChannel() != null) {
        filters.add(Filter.eq("paymentChannel", req.getPaymentChannel()));
        model.addAttribute("paymentChannel", req.getPaymentChannel());
    } 
    if (req.getBeginDate() != null) {
      Filter dateGeFilter =
          new Filter("createDate", Operator.ge, TimeUtils.formatDate2Day(req.getBeginDate()));
      filters.add(dateGeFilter);
      model.addAttribute("beginDate", req.getBeginDate());
    }
    if (req.getEndDate() != null) {
      Filter dateLeFilter =
          new Filter("createDate", Operator.le, TimeUtils.addDays(1,
              TimeUtils.formatDate2Day(req.getEndDate())));
      filters.add(dateLeFilter);
      model.addAttribute("endDate", req.getEndDate());
    }
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("page", leScoreRecordService.findPage(pageable));
    return "/leScoreRecord/list";
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    LeScoreRecord leScoreRecord = leScoreRecordService.find(id);
    model.addAttribute("leScoreRecord", leScoreRecord);
    return "/leScoreRecord/edit";
  }

  /**
   * 更新(提现审核)
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public @ResponseBody Message update(LeScoreRecord leScoreRecord) {
    return leScoreRecordService.auditWithdraw(leScoreRecord);
  }

  /**
   * 查看
   */
  @RequestMapping(value = "/details", method = RequestMethod.GET)
  public String details(Long id, ModelMap model) {
    LeScoreRecord leScoreRecord = leScoreRecordService.find(id);
    model.addAttribute("leScoreRecord", leScoreRecord);
    return "/leScoreRecord/details";
  }

  /**
   * 通联批量提现
   */
  @RequestMapping(value = "/batchWithdrawal", method = RequestMethod.POST)
  public @ResponseBody Message batchWithdrawal(Long[] ids) {
    if (ids == null || ids.length == 0) {
      return Message.error("请至少选择一条记录！");
    }
    //获取支付渠道
    PaymentChannel channel = CommonUtils.getPaymentChannel();

    //1. 通联支付渠道
    if (PaymentChannel.ALLINPAY == channel) {
        String reqSn = leScoreRecordService.batchWithdrawalByAllinpay(ids);
        if (reqSn != null) {
        	//隔一段时间 异步更新 提现记录状态
        	leScoreRecordJob.notifyWithdrawRecordByAllinpay(reqSn);
        } else {
        	return Message.success("批量提现执行失败!");
        }
	}
    //2. 九派支付渠道
    else if(PaymentChannel.JIUPAI == channel) {
    	leScoreRecordService.batchWithdrawalByJiuPai(ids);   
	}
    return Message.success("批量提现执行成功!");
  }

}

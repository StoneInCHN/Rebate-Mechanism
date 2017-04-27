package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.commonenum.CommonEnum.LeScoreType;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.request.LeScoreRecordReq;
import org.rebate.service.LeScoreRecordService;
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


  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, LeScoreRecordReq req, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    filters.add(Filter.eq("leScoreType", LeScoreType.WITHDRAW));
    if (req.getUserName() != null) {
      filters.add(Filter.like("endUser&userName", "%"+req.getUserName()+"%"));
    }
    if (req.getCellPhoneNum() != null) {
      filters.add(Filter.like("endUser&cellPhoneNum","%"+ req.getCellPhoneNum()+"%"));
    }
    if (req.getWithdrawStatus() != null) {
      filters.add(Filter.eq("withdrawStatus", req.getWithdrawStatus()));
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
    model.addAttribute("page", leScoreRecordService.findPage(pageable));
    model.addAttribute("withdrawStatus", req.getWithdrawStatus());
    model.addAttribute("userName", req.getUserName());
    model.addAttribute("beginDate", req.getBeginDate());
    model.addAttribute("endDate", req.getEndDate());
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
}

package org.rebate.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.EndUser;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.request.EndUserReq;
import org.rebate.service.EndUserService;
import org.rebate.service.LeBeanRecordService;
import org.rebate.service.LeMindRecordService;
import org.rebate.service.LeScoreRecordService;
import org.rebate.service.RebateRecordService;
import org.rebate.utils.LogUtil;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller("endUserController")
@RequestMapping("/console/endUser")
public class EndUserController extends BaseController {

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;

  @Resource(name = "rebateRecordServiceImpl")
  private RebateRecordService rebateRecordService;

  @Resource(name = "leMindRecordServiceImpl")
  private LeMindRecordService leMindRecordService;

  @Resource(name = "leScoreRecordServiceImpl")
  private LeScoreRecordService leScoreRecordService;

  @Resource(name = "leBeanRecordServiceImpl")
  private LeBeanRecordService leBeanRecordService;


  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, EndUserReq request, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (StringUtils.isNotEmpty(request.getCellPhoneNum())) {
      filters.add(Filter.like("cellPhoneNum", "%" + request.getCellPhoneNum() + "%"));
      model.addAttribute("cellPhoneNum", request.getCellPhoneNum());
    }
    if (StringUtils.isNotEmpty(request.getRecommenderMobile())) {
      filters.add(Filter.like("recommenderMobile", "%" + request.getRecommenderMobile() + "%"));
      model.addAttribute("recommenderMobile", request.getRecommenderMobile());
    }
    if (StringUtils.isNotEmpty(request.getNickName())) {
      filters.add(Filter.like("nickName", "%" + request.getNickName() + "%"));
      model.addAttribute("nickName", request.getNickName());
    }
    if (request.getAccountStatus() != null) {
      filters.add(Filter.eq("accountStatus", request.getAccountStatus()));
      model.addAttribute("accountStatus", request.getAccountStatus());
    }
    if (request.getRegDateFrom() != null) {
      filters.add(Filter.ge("createDate", TimeUtils.formatDate2Day(request.getRegDateFrom())));
      model.addAttribute("regDateFrom", request.getRegDateFrom());
    }
    if (request.getRegDateTo() != null) {
      filters.add(Filter.lt("createDate",
          TimeUtils.addDays(1, TimeUtils.formatDate2Day(request.getRegDateTo()))));
      model.addAttribute("regDateTo", request.getRegDateTo());
    }
    if (request.getIsSalesman() != null) {
      filters.add(Filter.eq("isSalesman", request.getIsSalesman()));
      model.addAttribute("isSalesman", request.getIsSalesman());
    }
    filters.add(Filter.ne("accountStatus", AccountStatus.DELETE));
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("page", endUserService.findPage(pageable));
    return "/endUser/list";
  }

  /**
   * 查看
   */
  @RequestMapping(value = "/details", method = RequestMethod.GET)
  public String details(Long id, ModelMap model) {
    EndUser endUser = endUserService.find(id);
    model.addAttribute("endUser", endUser);
    return "/endUser/details";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    List<EndUser> endUsers = new ArrayList<EndUser>();
    if (ids != null && ids.length > 0) {
      for (Long id : ids) {
        EndUser endUser = endUserService.find(id);
        endUser.setAccountStatus(AccountStatus.DELETE);
        endUsers.add(endUser);
      }
    }
    endUserService.update(endUsers);
    return SUCCESS_MESSAGE;
  }


  /**
   * 禁用
   */
  @RequestMapping(value = "/locked", method = RequestMethod.POST)
  public @ResponseBody Message locked(Long[] ids) {
    List<EndUser> endUsers = new ArrayList<EndUser>();
    if (ids != null && ids.length > 0) {
      for (Long id : ids) {
        EndUser endUser = endUserService.find(id);
        endUser.setAccountStatus(AccountStatus.LOCKED);
        endUsers.add(endUser);
      }
    }
    endUserService.update(endUsers);
    return SUCCESS_MESSAGE;
  }


  /**
   * 用户积分记录
   */
  @RequestMapping(value = "/score/record", method = RequestMethod.GET)
  public String scoreRecord(Pageable pageable, EndUserReq request, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (StringUtils.isNotEmpty(request.getCellPhoneNum())) {
      filters.add(Filter.like("endUser&cellPhoneNum", "%" + request.getCellPhoneNum() + "%"));
      model.addAttribute("cellPhoneNum", request.getCellPhoneNum());
    }
    if (StringUtils.isNotEmpty(request.getNickName())) {
      filters.add(Filter.like("endUser&nickName", "%" + request.getNickName() + "%"));
      model.addAttribute("nickName", request.getNickName());
    }
    if (StringUtils.isNotEmpty(request.getSellerName())) {
      filters.add(Filter.like("seller&name", "%" + request.getSellerName() + "%"));
      model.addAttribute("sellerName", request.getSellerName());
    }
    if (request.getRecordTimeFrom() != null) {
      filters.add(Filter.ge("createDate", TimeUtils.formatDate2Day(request.getRecordTimeFrom())));
      model.addAttribute("recordTimeFrom", request.getRecordTimeFrom());
    }
    if (request.getRecordTimeTo() != null) {
      filters.add(Filter.lt("createDate",
          TimeUtils.addDays(1, TimeUtils.formatDate2Day(request.getRecordTimeTo()))));
      model.addAttribute("recordTimeTo", request.getRecordTimeTo());
    }
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    orderings.add(Ordering.asc("orderId"));
    pageable.setOrders(orderings);
    model.addAttribute("page", rebateRecordService.findPage(pageable));
    return "/endUser/scoreRecord";
  }

  /**
   * 用户乐心记录
   */
  @RequestMapping(value = "/leMind/record", method = RequestMethod.GET)
  public String leMindRecord(Pageable pageable, EndUserReq request, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (StringUtils.isNotEmpty(request.getCellPhoneNum())) {
      filters.add(Filter.like("endUser&cellPhoneNum", "%" + request.getCellPhoneNum() + "%"));
      model.addAttribute("cellPhoneNum", request.getCellPhoneNum());
    }
    if (StringUtils.isNotEmpty(request.getNickName())) {
      filters.add(Filter.like("endUser&nickName", "%" + request.getNickName() + "%"));
      model.addAttribute("nickName", request.getNickName());
    }
    if (request.getStatus() != null) {
      filters.add(Filter.eq("status", request.getStatus()));
      model.addAttribute("status", request.getStatus());
    }
    if (request.getRecordTimeFrom() != null) {
      filters.add(Filter.ge("createDate", TimeUtils.formatDate2Day(request.getRecordTimeFrom())));
      model.addAttribute("recordTimeFrom", request.getRecordTimeFrom());
    }
    if (request.getRecordTimeTo() != null) {
      filters.add(Filter.lt("createDate",
          TimeUtils.addDays(1, TimeUtils.formatDate2Day(request.getRecordTimeTo()))));
      model.addAttribute("recordTimeTo", request.getRecordTimeTo());
    }
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("page", leMindRecordService.findPage(pageable));
    return "/endUser/leMindRecord";
  }

  /**
   * 用户乐分记录
   */
  @RequestMapping(value = "/leScore/record", method = RequestMethod.GET)
  public String leScoreRecord(Pageable pageable, EndUserReq request, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (StringUtils.isNotEmpty(request.getCellPhoneNum())) {
      filters.add(Filter.like("endUser&cellPhoneNum", "%" + request.getCellPhoneNum() + "%"));
      model.addAttribute("cellPhoneNum", request.getCellPhoneNum());
    }
    if (StringUtils.isNotEmpty(request.getNickName())) {
      filters.add(Filter.like("endUser&nickName", "%" + request.getNickName() + "%"));
      model.addAttribute("nickName", request.getNickName());
    }
    if (request.getLeScoreType() != null) {
      filters.add(Filter.eq("leScoreType", request.getLeScoreType()));
      model.addAttribute("leScoreType", request.getLeScoreType());
    }
    // if (StringUtils.isNotEmpty(request.getSellerName())) {
    // filters.add(Filter.eq("seller&name", "%" + request.getSellerName() + "%"));
    // model.addAttribute("sellerName", request.getSellerName());
    // }
    if (request.getRecordTimeFrom() != null) {
      filters.add(Filter.ge("createDate", TimeUtils.formatDate2Day(request.getRecordTimeFrom())));
      model.addAttribute("recordTimeFrom", request.getRecordTimeFrom());
    }
    if (request.getRecordTimeTo() != null) {
      filters.add(Filter.lt("createDate",
          TimeUtils.addDays(1, TimeUtils.formatDate2Day(request.getRecordTimeTo()))));
      model.addAttribute("recordTimeTo", request.getRecordTimeTo());
    }
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("page", leScoreRecordService.findPage(pageable));
    return "/endUser/leScoreRecord";
  }

  /**
   * 用户乐豆记录
   */
  @RequestMapping(value = "/leBean/record", method = RequestMethod.GET)
  public String leBeanRecord(Pageable pageable, EndUserReq request, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (StringUtils.isNotEmpty(request.getCellPhoneNum())) {
      filters.add(Filter.like("endUser&cellPhoneNum", "%" + request.getCellPhoneNum() + "%"));
      model.addAttribute("cellPhoneNum", request.getCellPhoneNum());
    }
    if (StringUtils.isNotEmpty(request.getNickName())) {
      filters.add(Filter.like("endUser&nickName", "%" + request.getNickName() + "%"));
      model.addAttribute("nickName", request.getNickName());
    }
    if (request.getType() != null) {
      filters.add(Filter.eq("type", request.getType()));
      model.addAttribute("type", request.getType());
    }
    if (request.getRecordTimeFrom() != null) {
      filters.add(Filter.ge("createDate", TimeUtils.formatDate2Day(request.getRecordTimeFrom())));
      model.addAttribute("recordTimeFrom", request.getRecordTimeFrom());
    }
    if (request.getRecordTimeTo() != null) {
      filters.add(Filter.lt("createDate",
          TimeUtils.addDays(1, TimeUtils.formatDate2Day(request.getRecordTimeTo()))));
      model.addAttribute("recordTimeTo", request.getRecordTimeTo());
    }
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("page", leBeanRecordService.findPage(pageable));
    return "/endUser/leBeanRecord";
  }

  /**
   * 设置业务员
   */
  @RequestMapping(value = "/updateSalesMan", method = RequestMethod.POST)
  public @ResponseBody Message updateSalesMan(Long id, Boolean isSalesman) {
    if (id == null || isSalesman == null) {
      return ERROR_MESSAGE;
    }
    EndUser endUser = endUserService.find(id);
    endUser.setIsSalesman(isSalesman);
    endUser.setIsSalesmanApply(true);
    endUserService.update(endUser);
    return SUCCESS_MESSAGE;
  }

  /**
   * 设置业务员上传商家审核功能
   */
  @RequestMapping(value = "/updateSalesManApply", method = RequestMethod.POST)
  public @ResponseBody Message updateSalesManApply(Long id, Boolean isSalesmanApply) {
    if (id == null || isSalesmanApply == null) {
      return ERROR_MESSAGE;
    }
    EndUser endUser = endUserService.find(id);
    endUser.setIsSalesmanApply(isSalesmanApply);
    endUserService.update(endUser);
    return SUCCESS_MESSAGE;
  }


  /**
   * 修改用户乐心(用户特殊需求)
   */
  @RequestMapping(value = "/updateLeMind", method = RequestMethod.POST)
  public @ResponseBody Message updateLeMind(Long id, BigDecimal mindAmount) {
    if (id == null || mindAmount == null) {
      return ERROR_MESSAGE;
    }
    EndUser endUser = endUserService.find(id);
    LogUtil.debug(EndUserController.class, "updateLeMind",
        "Force update user mind! UserId: %s,CellPhone: %s,originalMinds: %s, updatedMinds: %s", id,
        endUser.getCellPhoneNum(), endUser.getCurLeMind(), mindAmount);
    endUser.setCurLeMind(mindAmount);
    endUserService.update(endUser);
    return SUCCESS_MESSAGE;
  }

}

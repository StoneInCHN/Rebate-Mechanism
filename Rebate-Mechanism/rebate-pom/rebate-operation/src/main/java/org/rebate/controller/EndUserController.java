package org.rebate.controller;

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

}

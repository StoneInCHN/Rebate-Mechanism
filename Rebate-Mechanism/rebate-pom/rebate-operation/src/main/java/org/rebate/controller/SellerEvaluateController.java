package org.rebate.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.SellerEvaluate;
import org.rebate.entity.commonenum.CommonEnum.CommonStatus;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.paging.Pageable;
import org.rebate.request.SellerEvaluateRequest;
import org.rebate.service.SellerEvaluateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("console/sellerEvaluate")
@Controller("sellerEvaluateController")
public class SellerEvaluateController extends BaseController {

  @Resource(name="sellerEvaluateServiceImpl")
  private SellerEvaluateService sellerEvaluateService;
  
  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable,SellerEvaluateRequest request, ModelMap model) {

    List<Filter> filters = new ArrayList<Filter>();
    if (request.getUserName() != null) {
      Filter userNameFilter = new Filter("endUser&nickName", Operator.like, "%"+request.getUserName()+"%");
      filters.add(userNameFilter);
    }
    if (request.getSellerName() != null) {
      Filter sellerFilter = new Filter("seller&name", Operator.like, "%"+request.getSellerName()+"%");
      filters.add(sellerFilter);
    }
    if (request.getScore() != null) {
      Filter scoreFilter = new Filter("score", Operator.eq,request.getScore());
      filters.add(scoreFilter);
    }
    if (request.getBeginDate() != null) {
      Filter dateGeFilter = new Filter("createDate", Operator.ge, request.getBeginDate() );
      filters.add(dateGeFilter);
    }
    if (request.getEndDate() != null) {
      Filter dateLeFilter = new Filter("createDate", Operator.le, request.getEndDate());
      filters.add(dateLeFilter);
    }
    pageable.setFilters(filters);
    model.addAttribute("page", sellerEvaluateService.findPage(pageable));
    model.addAttribute("beginDate", request.getBeginDate());
    model.addAttribute("endDate", request.getEndDate());
    model.addAttribute("userName", request.getUserName());
    model.addAttribute("sellerName", request.getSellerName());
    model.addAttribute("score", request.getScore());
    return "/sellerEvaluate/list";
  }
  
  /**
   * 启用
   */
  @RequestMapping(value = "/active", method = RequestMethod.POST)
  public @ResponseBody Message active(Long[] ids) {
    if(ids==null || ids.length <1){
      return ERROR_MESSAGE;
    }
    List<SellerEvaluate> lists = new ArrayList<SellerEvaluate>();
    List<SellerEvaluate> sellerEvaluates = sellerEvaluateService.findList(ids);
    for (SellerEvaluate sellerEvaluate : sellerEvaluates) {
      sellerEvaluate.setStatus(CommonStatus.ACITVE);
      lists.add(sellerEvaluate);
    }
    sellerEvaluateService.update(lists);
    return SUCCESS_MESSAGE;
  }
  
  /**
   * 禁用
   */
  @RequestMapping(value = "/inActive", method = RequestMethod.POST)
  public @ResponseBody Message inActive(Long[] ids) {
    if(ids==null || ids.length <1){
      return ERROR_MESSAGE;
    }
    List<SellerEvaluate> lists = new ArrayList<SellerEvaluate>();
    List<SellerEvaluate> sellerEvaluates = sellerEvaluateService.findList(ids);
    for (SellerEvaluate sellerEvaluate : sellerEvaluates) {
      sellerEvaluate.setStatus(CommonStatus.INACTIVE);
      lists.add(sellerEvaluate);
    }
    sellerEvaluateService.update(lists);
    return SUCCESS_MESSAGE;
  }
  
  /**
   * 详情
   */
  @RequestMapping(value = "/details", method = RequestMethod.GET)
  public String details(Long id, ModelMap model) {
    model.addAttribute("sellerEvaluate", sellerEvaluateService.find(id));
    return "/sellerEvaluate/details";
  }
  
}

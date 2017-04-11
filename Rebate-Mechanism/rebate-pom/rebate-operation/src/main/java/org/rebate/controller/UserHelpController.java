package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.Seller;
import org.rebate.entity.UserHelp;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.paging.Pageable;
import org.rebate.request.UserHelpReq;
import org.rebate.service.UserHelpService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("userHelpController")
@RequestMapping("console/userHelp")
public class UserHelpController extends BaseController {

  @Resource(name = "userHelpServiceImpl")
  private UserHelpService userHelpService;

  /**
   * 添加
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add() {
    return "/userHelp/add";
  }

  /**
   * 保存
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public String save(UserHelp userHelp) {
    if (!isValid(userHelp)) {
      return ERROR_VIEW;
    }
    userHelpService.save(userHelp);
    return "redirect:list.jhtml";
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("userHelp", userHelpService.find(id));
    return "/userHelp/edit";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String update(UserHelp userHelp) {
    if (!isValid(userHelp)) {
      return ERROR_VIEW;
    }
    userHelpService.update(userHelp);
    return "redirect:list.jhtml";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable,UserHelpReq request, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if(request.getTitle()!=null){
      filters.add(Filter.eq("title", request.getTitle()));
      model.addAttribute("title", request.getTitle());
    }
    if(request.getIsEnabled()!=null){
      filters.add(Filter.eq("isEnabled", request.getIsEnabled()));
      model.addAttribute("isEnabled", request.getIsEnabled());
    }
    pageable.setFilters(filters);
    model.addAttribute("page", userHelpService.findPage(pageable));
    return "/userHelp/list";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    if (ids.length >= userHelpService.count()) {
      return Message.error("rebate.common.deleteAllNotAllowed");
    }
    userHelpService.delete(ids);
    return SUCCESS_MESSAGE;
  }

  /**
   * 查看详情
   */
  @RequestMapping(value = "/details", method = RequestMethod.GET)
  public String details(Long id, ModelMap model) {
    UserHelp userHelp = userHelpService.find(id);
    model.addAttribute("userHelp", userHelp);
    return "/userHelp/details";
  }

}

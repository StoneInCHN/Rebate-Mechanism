package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.MessageInfo;
import org.rebate.entity.UserHelp;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.paging.Pageable;
import org.rebate.request.UserHelpReq;
import org.rebate.service.MessageInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("console/messageInfo")
@Controller("messageInfoController")
public class MessageInfoController extends BaseController{

  @Resource(name="messageInfoServiceImpl")
  private MessageInfoService messageInfoService;
 
  /**
   * 添加
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add() {
    return "/messageInfo/add";
  }

  /**
   * 保存
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public String save(MessageInfo messageInfo) {
    if (!isValid(messageInfo)) {
      return ERROR_VIEW;
    }
    messageInfoService.save(messageInfo);
    return "redirect:list.jhtml";
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("messageInfo", messageInfoService.find(id));
    return "/messageInfo/edit";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String update(MessageInfo messageInfo) {
    if (!isValid(messageInfo)) {
      return ERROR_VIEW;
    }
    messageInfoService.update(messageInfo);
    return "redirect:list.jhtml";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    pageable.setFilters(filters);
    model.addAttribute("page", messageInfoService.findPage(pageable));
    return "/messageInfo/list";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    messageInfoService.delete(ids);
    return SUCCESS_MESSAGE;
  }

  /**
   * 查看详情
   */
  @RequestMapping(value = "/details", method = RequestMethod.GET)
  public String details(Long id, ModelMap model) {
    MessageInfo messageInfo = messageInfoService.find(id);
    model.addAttribute("messageInfo", messageInfo);
    return "/messageInfo/details";
  }
  
}

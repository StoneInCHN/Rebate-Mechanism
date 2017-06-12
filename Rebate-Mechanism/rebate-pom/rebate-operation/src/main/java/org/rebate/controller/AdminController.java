package org.rebate.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.Admin;
import org.rebate.entity.Role;
import org.rebate.entity.base.BaseEntity.Save;
import org.rebate.entity.commonenum.CommonEnum.AdminStatus;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.paging.Pageable;
import org.rebate.service.AdminService;
import org.rebate.service.RoleService;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 管理员
 * 
 */
@Controller("adminController")
@RequestMapping("/console/admin")
public class AdminController extends BaseController {

  @Resource(name = "adminServiceImpl")
  private AdminService adminService;
  @Resource(name = "roleServiceImpl")
  private RoleService roleService;


  /**
   * 检查用户名是否存在
   */
  @RequestMapping(value = "/check_username", method = RequestMethod.GET)
  public @ResponseBody boolean checkUsername(String username) {
    if (StringUtils.isEmpty(username)) {
      return false;
    }
    if (adminService.usernameExists(username)) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * 添加
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add(ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    filters.add(Filter.ne("", ""));
    model.addAttribute("roles", roleService.findList(null, filters, null));
    model.addAttribute("adminStatusTypes", AdminStatus.values());
    return "/admin/add";
  }

  /**
   * 保存
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public String save(Admin admin, Long[] roleIds) {
    admin.setRoles(new HashSet<Role>(roleService.findList(roleIds)));
    if (!isValid(admin, Save.class)) {
      return ERROR_VIEW;
    }
    if (adminService.usernameExists(admin.getUsername())) {
      return ERROR_VIEW;
    }
    admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
    admin.setIsSystem(false);
    adminService.save(admin);
    return "redirect:list.jhtml";
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("roles", roleService.findAll());
    model.addAttribute("admin", adminService.find(id));
    model.addAttribute("adminStatusTypes", AdminStatus.values());
    return "/admin/edit";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String update(Admin admin, Long[] roleIds, RedirectAttributes redirectAttributes) {
    admin.setRoles(new HashSet<Role>(roleService.findList(roleIds)));
    if (!isValid(admin)) {
      return ERROR_VIEW;
    }
    Admin pAdmin = adminService.find(admin.getId());
    if (pAdmin == null) {
      return ERROR_VIEW;
    }
    if (StringUtils.isNotEmpty(admin.getPassword())) {
      admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
    } else {
      admin.setPassword(pAdmin.getPassword());
    }
    adminService.update(admin, "username", "loginDate", "loginIp", "isSystem");

    return "redirect:list.jhtml";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, String username, String name, String email, Date loginDate,
      AdminStatus adminStatus, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (username != null) {
      filters.add(Filter.like("username", username));
      model.addAttribute("username", username);
    }
    if (name != null) {
      filters.add(Filter.like("name", name));
      model.addAttribute("name", name);
    }
    if (email != null) {
      filters.add(Filter.like("email", email));
      model.addAttribute("email", email);
    }
    if (loginDate != null) {
      Date formDate = TimeUtils.formatDate2Day(loginDate);
      Date toDate = TimeUtils.addDays(1, loginDate);
      filters.add(Filter.ge("loginDate", formDate));
      filters.add(Filter.le("loginDate", toDate));
      model.addAttribute("loginDate", loginDate);
    }
    if (adminStatus != null) {
      filters.add(Filter.eq("adminStatus", adminStatus));
      model.addAttribute("adminStatus", adminStatus);
    }
    filters.add(Filter.ne("username", "superadmin"));
    pageable.setFilters(filters);
    model.addAttribute("page", adminService.findPage(pageable));
    return "/admin/list";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    if (ids.length >= adminService.count()) {
      return Message.error("admin.common.deleteAllNotAllowed");
    }
    adminService.delete(ids);
    return SUCCESS_MESSAGE;
  }


}

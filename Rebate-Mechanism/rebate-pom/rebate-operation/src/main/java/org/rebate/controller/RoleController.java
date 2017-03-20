package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.beans.Setting;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.Role;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.paging.Pageable;
import org.rebate.service.AdminService;
import org.rebate.service.RoleService;
import org.rebate.utils.SettingUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 角色
 * 
 */
@Controller("roleController")
@RequestMapping("/console/role")
public class RoleController extends BaseController {

  @Resource(name = "roleServiceImpl")
  private RoleService roleService;

  @Resource(name = "adminServiceImpl")
  private AdminService adminService;

  /**
   * 添加
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add() {
    return "/role/add";
  }

  /**
   * 保存
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public String save(Role role) {
    role.setIsSystem(false);
    roleService.save(role);
    return "redirect:list.jhtml";
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    Role role = roleService.find(id);
    Setting setting = SettingUtils.get();
    model.addAttribute("role", roleService.find(id));
    if (setting.getDefaultDistributorRoleId().equals(role.getId())) {
      return "/role/edit4distributor";
    } else {
      return "/role/edit";
    }
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String update(Role role) {
    Role pRole = roleService.find(role.getId());
    if (pRole == null || pRole.getIsSystem()) {
      return ERROR_VIEW;
    }
    roleService.update(role, "isSystem", "admins", "systemType");
    return "redirect:list.jhtml";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    Filter filter = new Filter();
    filter.setProperty("systemType");
    filter.setOperator(Operator.eq);
    filters.add(filter);
    pageable.setFilters(filters);
    model.addAttribute("page", roleService.findPage(pageable));
    return "/role/list";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    if (ids != null) {
      for (Long id : ids) {
        Role role = roleService.find(id);
        if (role != null && role.getIsSystem()) {
          return Message.error("csh.role.deleteSystemNotAllowed", role.getName());
        } else if (role != null && roleService.hasContainAdmin(role)) {
          return Message.error("csh.role.deleteExistNotAllowed", role.getName());
        }
      }
      roleService.delete(ids);
    }
    return SUCCESS_MESSAGE;
  }

}

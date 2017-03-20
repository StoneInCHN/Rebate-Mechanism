package org.rebate.controller;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.Area;
import org.rebate.service.AreaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("areaController")
@RequestMapping("/console/area")
public class AreaController extends BaseController {

  @Resource(name = "areaServiceImpl")
  private AreaService areaService;

  /**
   * 添加
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add(Long parentId, ModelMap model) {
    model.addAttribute("parent", areaService.find(parentId));
    return "/area/add";
  }

  /**
   * 保存
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public String save(Area area, Long parentId, RedirectAttributes redirectAttributes) {
    area.setParent(areaService.find(parentId));
    if (!isValid(area)) {
      return ERROR_VIEW;
    }
    area.setFullName(null);
    area.setTreePath(null);
    area.setChildren(null);
    areaService.save(area);
    // addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
    if (parentId != null) {
      return "redirect:list.jhtml?parentId=" + parentId;
    } else {
      return "redirect:list.jhtml";
    }

  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("area", areaService.find(id));
    return "/area/edit";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String update(Area area, RedirectAttributes redirectAttributes) {
    if (!isValid(area)) {
      return ERROR_VIEW;
    }
    areaService.update(area, "fullName", "treePath", "parent", "children", "members", "receivers",
        "orders", "deliveryCenters");
    // addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
    return "redirect:list.jhtml";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Long parentId, ModelMap model) {
    Area parent = areaService.find(parentId);
    if (parent != null) {
      model.addAttribute("parent", parent);
      model.addAttribute("areas", new ArrayList<Area>(parent.getChildren()));
    } else {
      model.addAttribute("areas", areaService.findRoots());
    }
    return "/area/list";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long id) {
    areaService.delete(id);
    return SUCCESS_MESSAGE;
  }

}

package org.rebate.controller;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.Area;
import org.rebate.entity.HotCity;
import org.rebate.service.AreaService;
import org.rebate.service.HotCityService;
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
  
  @Resource(name="hotCityServiceImpl")
  private HotCityService hotCityService;

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
  public String update(Area area) {
    if (!isValid(area)) {
      return ERROR_VIEW;
    }
    Area temp = areaService.find(area.getId());
    if(temp!=null){
      temp.setName(area.getName());
      temp.setPyName(area.getPyName());
      temp.setOrder(area.getOrder());
      temp.setIsCity(area.getIsCity());
      areaService.update(temp);
    }
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
    model.addAttribute("hotCitys", hotCityService.findAll());
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
  
  /**
   * 添加
   */
  @RequestMapping(value = "/addHotCity", method = RequestMethod.GET)
  public String addHotCity(Long parentId, ModelMap model) {
    return "/area/addHotCity";
  }
  
  /**
   * 添加
   */
  @RequestMapping(value = "/saveHotCity", method = RequestMethod.POST)
  public @ResponseBody Message saveHotCity(Long areaId, HotCity hotCity) {
    if(areaId!=null){
      Area area = areaService.find(areaId);
      if(area!=null){
        hotCity.setCityId(areaId);
        hotCity.setCityName(area.getName());
        hotCityService.save(hotCity);
        return SUCCESS_MESSAGE;
      }
    }
   return ERROR_MESSAGE;
  }
  
  /**
   * 删除
   */
  @RequestMapping(value = "/deleteHotCity", method = RequestMethod.POST)
  public @ResponseBody Message deleteHotCity(Long id) {
    hotCityService.delete(id);
    return SUCCESS_MESSAGE;
  }
  

}

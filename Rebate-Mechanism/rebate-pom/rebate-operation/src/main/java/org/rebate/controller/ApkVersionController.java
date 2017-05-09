package org.rebate.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.ApkVersion;
import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.entity.commonenum.CommonEnum.FileType;
import org.rebate.framework.entity.BaseEntity.Save;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.service.ApkVersionService;
import org.rebate.service.FileService;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("apkVersionController")
@RequestMapping("/console/apkVersion")
public class ApkVersionController extends BaseController {


  @Resource(name = "apkVersionServiceImpl")
  private ApkVersionService apkVersionService;

  @Resource(name = "fileServiceImpl")
  private FileService fileService;


  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, String versionName, Boolean isForced, Date uploadDateFrom,
      Date uploadDateTo, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (StringUtils.isNotEmpty(versionName)) {
      filters.add(Filter.like("versionName", "%" + versionName + "%"));
      model.addAttribute("versionName", versionName);
    }
    if (isForced != null) {
      filters.add(Filter.eq("isForced", isForced));
      model.addAttribute("isForced", isForced);
    }
    if (uploadDateFrom != null) {
      filters.add(Filter.ge("createDate", TimeUtils.formatDate2Day(uploadDateFrom)));
      model.addAttribute("uploadDateFrom", uploadDateFrom);
    }
    if (uploadDateTo != null) {
      filters.add(Filter.lt("createDate",
          TimeUtils.addDays(1, TimeUtils.formatDate2Day(uploadDateTo))));
      model.addAttribute("uploadDateTo", uploadDateTo);
    }
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("page", apkVersionService.findPage(pageable));
    return "/apkVersion/list";
  }

  /**
   * 添加
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add(ModelMap model) {
    return "/apkVersion/add";
  }

  /**
   * 检查版本号是否存在
   */
  @RequestMapping(value = "/checkVersion", method = RequestMethod.GET)
  public @ResponseBody boolean checkVersion(String versionName, Long id) {
    if (StringUtils.isEmpty(versionName)) {
      return false;
    }
    return !apkVersionService.versionExists(versionName, id);
  }

  /**
   * 保存
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public String save(ApkVersion apkVersion) {
    if (!isValid(apkVersion, Save.class)) {
      return ERROR_VIEW;
    }
    if (apkVersion.getFile().getSize() < 1) {
      return ERROR_VIEW;
    } else {
      if (!fileService.isValid(FileType.file, apkVersion.getFile())) {
        return ERROR_VIEW;
      } else {
        String apkPath = fileService.uploadApk(apkVersion.getFile());
        apkVersion.setApkPath(apkPath);
        apkVersion.setVersionCode(apkVersion.getVersionCode());
        // ApkVersion cuApkVersion = apkVersionService.getCurNewApkVersion();
        // if (cuApkVersion != null) {
        // apkVersion.setVersionCode(cuApkVersion.getVersionCode() + 1);
        // }
        apkVersion.setAppPlatform(AppPlatform.ANDROID);
        apkVersionService.save(apkVersion);
        return "redirect:list.jhtml";
      }

    }

  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("apkVersion", apkVersionService.find(id));
    return "/apkVersion/edit";
  }

  // /**
  // * 更新
  // */
  // @RequestMapping(value = "/update", method = RequestMethod.POST)
  // public String update(HttpServletRequest request, RedirectAttributes redirectAttributes,
  // ApkVersion apkVersion) {
  // if (!isValid(apkVersion)) {
  // return ERROR_VIEW;
  // }
  // if (apkVersion.getFile().getSize() > 0) {
  // String apkPath = fileService.upload(FileType.file, apkVersion.getFile(), true);
  // apkVersion.setApkPath(apkPath);
  // }
  // apkVersionService.update(apkVersion);
  // return "redirect:list.jhtml";
  // }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    if (ids != null && ids.length > 0) {
      apkVersionService.delete(ids);
    }
    return SUCCESS_MESSAGE;
  }



}

package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.beans.Setting;
import org.rebate.beans.Setting.ImageType;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.TopBanner;
import org.rebate.entity.commonenum.CommonEnum.FileType;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.paging.Pageable;
import org.rebate.service.FileService;
import org.rebate.service.TopBannerService;
import org.rebate.utils.SettingUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("topBannerController")
@RequestMapping("/console/topBanner")
public class TopBannerController extends BaseController {

  @Resource(name = "topBannerServiceImpl")
  private TopBannerService topBannerService;

  @Resource(name = "fileServiceImpl")
  private FileService fileService;

  /**
   * 添加
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add(ModelMap model) {
    return "/topBanner/add";
  }

  /**
   * 保存
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public @ResponseBody Message save(TopBanner topBanner) {

    if (!isValid(topBanner)) {
      return ERROR_MESSAGE;
    }
    String pictureUrl = "";
    if (topBanner.getBannerPicture() != null
        && fileService.isValid(FileType.image, topBanner.getBannerPicture())) {
      Setting setting = SettingUtils.get();
      if (topBanner.getBannerPicture().getSize() > setting.getImageMaxSize()) {
        return Message.error(message("rebate.topBanner.imageMaxSize"));
      }
      pictureUrl = fileService.saveImage(topBanner.getBannerPicture(), ImageType.ADVERTISEMENT);
    } else {
      return Message.error(message("rebate.topBanner.image.file.type.error"));
    }
    topBanner.setBannerUrl(pictureUrl);

    topBannerService.save(topBanner);
    return Message.success("rebate.topBanner.image.file.upload.success");
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("topBanner", topBannerService.find(id));
    return "/topBanner/edit";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public @ResponseBody Message update(TopBanner topBanner) {
    if (!isValid(topBanner)) {
      return ERROR_MESSAGE;
    }
    if(topBanner.getBannerPicture()!=null && topBanner.getBannerPicture().getSize() >0){
      if (fileService.isValid(FileType.image, topBanner.getBannerPicture())) {
          Setting setting = SettingUtils.get();
          if (topBanner.getBannerPicture().getSize() > setting.getImageMaxSize()) {
                return Message.error(message("rebate.topBanner.imageMaxSize"));
          }
       String pictureUrl = fileService.saveImage(topBanner.getBannerPicture(), ImageType.ADVERTISEMENT);
       topBanner.setBannerUrl(pictureUrl);
      }else {
        return Message.error(message("rebate.topBanner.image.file.type.error"));
      }
    }
    topBannerService.update(topBanner);
    return Message.success("rebate.topBanner.image.file.upload.success");
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    pageable.setFilters(filters);
    model.addAttribute("page", topBannerService.findPage(pageable));
    return "/topBanner/list";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    if (ids.length >= topBannerService.count()) {
      return Message.error("rebate.common.deleteAllNotAllowed");
    }
    topBannerService.delete(ids);
    return SUCCESS_MESSAGE;
  }
}

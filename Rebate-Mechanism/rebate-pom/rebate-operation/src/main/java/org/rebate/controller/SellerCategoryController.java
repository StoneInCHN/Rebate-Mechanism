package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.beans.Setting;
import org.rebate.beans.Setting.ImageType;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerCategory;
import org.rebate.entity.commonenum.CommonEnum.FileType;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.request.SellerCategoryReq;
import org.rebate.service.FileService;
import org.rebate.service.SellerCategoryService;
import org.rebate.utils.SettingUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller("sellerCategoryController")
@RequestMapping("console/sellerCategory")
public class SellerCategoryController extends BaseController {

  @Resource(name = "sellerCategoryServiceImpl")
  private SellerCategoryService sellerCategoryService;

  @Resource(name = "fileServiceImpl")
  private FileService fileService;

  /**
   * 添加
   */
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add(ModelMap model) {
    return "/sellerCategory/add";
  }

  /**
   * 保存
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public @ResponseBody Message save(SellerCategory sellerCategory) {

    if (!isValid(sellerCategory)) {
      return ERROR_MESSAGE;
    }
    String pictureUrl = "";
    if (sellerCategory.getCategoryPic() != null
        && fileService.isValid(FileType.image, sellerCategory.getCategoryPic())) {
      Setting setting = SettingUtils.get();
      if (sellerCategory.getCategoryPic().getSize() > setting.getImageMaxSize()) {
        return Message.error(message("rebate.common.imageMaxSize"));
      }
      pictureUrl = fileService.saveImage(sellerCategory.getCategoryPic(), ImageType.SELLERCATEGORY);
    } else {
      return Message.error(message("rebate.common.image.file.type.error"));
    }
    sellerCategory.setCategoryPicUrl(pictureUrl);

    sellerCategoryService.save(sellerCategory);
    return Message.success("rebate.common.image.file.upload.success");
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("sellerCategory", sellerCategoryService.find(id));
    return "/sellerCategory/edit";
  }

  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public @ResponseBody Message update(SellerCategory sellerCategory) {
    if (!isValid(sellerCategory)) {
      return ERROR_MESSAGE;
    }
    if (sellerCategory.getCategoryPic() != null && sellerCategory.getCategoryPic().getSize() > 0) {
      if (fileService.isValid(FileType.image, sellerCategory.getCategoryPic())) {
        Setting setting = SettingUtils.get();
        if (sellerCategory.getCategoryPic().getSize() > setting.getImageMaxSize()) {
          return Message.error(message("rebate.common.imageMaxSize"));
        }
        String pictureUrl =
            fileService.saveImage(sellerCategory.getCategoryPic(), ImageType.SELLERCATEGORY);
        sellerCategory.setCategoryPicUrl(pictureUrl);
      } else {
        return Message.error(message("rebate.common.image.file.type.error"));
      }
    }
    sellerCategoryService.update(sellerCategory);
    return Message.success("rebate.common.image.file.upload.success");
  }


  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable,SellerCategoryReq request,ModelMap model) {
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.asc("categorOrder"));
  
    List<Filter> filters = new ArrayList<Filter>();
    if(request.getCategoryName()!=null){
      filters.add(Filter.eq("categoryName", request.getCategoryName()));
      model.addAttribute("categoryName", request.getCategoryName());
    }
    if(request.getIsActive()!=null){
      filters.add(Filter.eq("isActive", request.getIsActive()));
      model.addAttribute("isActive", request.getIsActive());
    }
    pageable.setFilters(filters);
    pageable.setOrders(orderings);
    model.addAttribute("page", sellerCategoryService.findPage(pageable));
    
    return "/sellerCategory/list";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    if (ids != null) {
      for (Long id : ids) {
        SellerCategory sellerCategory = sellerCategoryService.find(id);
        Set<Seller> sellers = sellerCategory.getSellers();
        if (sellers != null && sellers.size() > 0) {
          return ERROR_MESSAGE;
        } else {
          sellerCategoryService.delete(id);
        }
      }

    }
    return SUCCESS_MESSAGE;
  }

}

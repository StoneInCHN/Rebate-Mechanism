package org.rebate.controller;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.rebate.beans.Message;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.Area;
import org.rebate.entity.Seller;
import org.rebate.entity.commonenum.CommonEnum.AccountStatus;
import org.rebate.entity.commonenum.CommonEnum.ImageSize;
import org.rebate.entity.commonenum.CommonEnum.QrCodeType;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.paging.Pageable;
import org.rebate.request.SellerRequest;
import org.rebate.service.AreaService;
import org.rebate.service.SellerCategoryService;
import org.rebate.service.SellerService;
import org.rebate.utils.DateUtils;
import org.rebate.utils.ExportUtils;
import org.rebate.utils.LogUtil;
import org.rebate.utils.QRCodeGenerator;
import org.rebate.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("sellerController")
@RequestMapping("/console/seller")
public class SellerController extends BaseController {

  @Resource(name = "sellerServiceImpl")
  private SellerService sellerService;

  @Resource(name = "sellerCategoryServiceImpl")
  private SellerCategoryService sellerCategoryService;

  @Resource(name = "areaServiceImpl")
  private AreaService areaService;
  
  @Resource(name = "taskExecutor")
  private Executor threadPoolExecutor;
  
  
  /**
   * 列表
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Pageable pageable, SellerRequest request, ModelMap model) {
    List<Filter> filters = new ArrayList<Filter>();
    if (StringUtils.isNotEmpty(request.getName())) {
      filters.add(Filter.like("name", request.getName()));
      model.addAttribute("name", request.getName());
    }
    if (request.getIsBeanPay() != null) {
      filters.add(Filter.eq("isBeanPay", request.getIsBeanPay()));
      model.addAttribute("isBeanPay", request.getIsBeanPay());
    }
    if (StringUtils.isNotEmpty(request.getLicenseNum())) {
      filters.add(Filter.like("licenseNum", request.getLicenseNum()));
      model.addAttribute("licenseNum", request.getLicenseNum());
    }
    if (StringUtils.isNotEmpty(request.getContactCellPhone())) {
      filters.add(Filter.like("contactCellPhone", request.getContactCellPhone()));
      model.addAttribute("contactCellPhone", request.getContactCellPhone());
    }
    if (StringUtils.isNotEmpty(request.getCellPhoneNum())) {
      filters.add(Filter.like("endUser&cellPhoneNum", "%" + request.getCellPhoneNum() + "%"));
      model.addAttribute("cellPhoneNum", request.getCellPhoneNum());
    }
    if (StringUtils.isNotEmpty(request.getContactPerson())) {
      filters.add(Filter.like("contactPerson", request.getContactPerson()));
      model.addAttribute("contactPerson", request.getContactPerson());
    }
    if (request.getAreaId() != null) {
      filters.add(Filter.eq("area", request.getAreaId()));
      model.addAttribute("areaId", request.getAreaId());
    }
    if (request.getSellerCategoryId() != null) {
      filters.add(Filter.eq("sellerCategory", request.getSellerCategoryId()));
      model.addAttribute("sellerCategoryId", request.getSellerCategoryId());
    }
    if (request.getAccountStatus() != null) {
      filters.add(Filter.eq("accountStatus", request.getAccountStatus()));
      model.addAttribute("accountStatus", request.getAccountStatus());
    }
    if (request.getApplyFromDate() != null) {
      filters.add(Filter.ge("createDate", TimeUtils.formatDate2Day(request.getApplyFromDate())));
      model.addAttribute("applyFromDate", request.getApplyFromDate());
    }
    if (request.getApplyToDate() != null) {
      filters.add(Filter.le("createDate",
          TimeUtils.addDays(1, TimeUtils.formatDate2Day(request.getApplyToDate()))));
      model.addAttribute("applyToDate", request.getApplyToDate());
    }
    filters.add(Filter.ne("accountStatus", AccountStatus.DELETE));
    pageable.setFilters(filters);
    List<Ordering> orderings = new ArrayList<Ordering>();
    orderings.add(Ordering.desc("createDate"));
    pageable.setOrders(orderings);
    model.addAttribute("sellerCategorys", sellerCategoryService.findAll());
    model.addAttribute("page", sellerService.findPage(pageable));
    return "/seller/list";
  }


  /**
   * 更新
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String update(Seller seller, Long areaId) {
    Seller temp = sellerService.find(seller.getId());
    temp.setAccountStatus(seller.getAccountStatus());
    temp.setAddress(seller.getAddress());
    if (areaId != null) {
      Area area = areaService.find(areaId);
      if (area != null) {
        temp.setArea(area);
      }
    }
    temp.setContactCellPhone(seller.getContactCellPhone());
    temp.setIsBeanPay(seller.getIsBeanPay());
    temp.setAvgPrice(seller.getAvgPrice());
    temp.setDescription(seller.getDescription());
    temp.setDiscount(seller.getDiscount());
    temp.setLongitude(seller.getLongitude());
    temp.setLatitude(seller.getLatitude());
    temp.setBusinessTime(seller.getBusinessTime());
    temp.setRemark(seller.getRemark());
    temp.setLimitAmountByDay(seller.getLimitAmountByDay());
    sellerService.update(temp);
    return "redirect:list.jhtml";
  }


  /**
   * 查看
   */
  @RequestMapping(value = "/details", method = RequestMethod.GET)
  public String details(Long id, ModelMap model) {
    Seller seller = sellerService.find(id);
    model.addAttribute("seller", seller);
    model.addAttribute("envImages", seller.getEnvImages());
    return "/seller/details";
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public @ResponseBody Message delete(Long[] ids) {
    List<Seller> sellers = new ArrayList<Seller>();
    if (ids != null && ids.length > 0) {
      for (Long id : ids) {
        Seller seller = sellerService.find(id);
        seller.setAccountStatus(AccountStatus.DELETE);
        sellers.add(seller);
      }
    }
    sellerService.update(sellers);
    return SUCCESS_MESSAGE;
  }

  /**
   * 编辑
   */
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public String edit(Long id, ModelMap model) {
    model.addAttribute("seller", sellerService.find(id));
    return "/seller/edit";
  }


  /**
   * 禁用
   */
  @RequestMapping(value = "/locked", method = RequestMethod.POST)
  public @ResponseBody Message locked(Long[] ids) {
    List<Seller> sellers = new ArrayList<Seller>();
    if (ids != null && ids.length > 0) {
      for (Long id : ids) {
        Seller seller = sellerService.find(id);
        seller.setAccountStatus(AccountStatus.LOCKED);
        sellers.add(seller);
      }
    }
    sellerService.update(sellers);
    return SUCCESS_MESSAGE;
  }

  /**
   * 查看商家在百度地图中的位置点
   */
  @RequestMapping(value = "/viewPosition", method = RequestMethod.GET)
  public String viewPosition(Long id, ModelMap model) {
    Seller seller = sellerService.find(id);
    model.addAttribute("seller", seller);
    return "/seller/viewPosition";
  }

  /**
   * 编辑商家在百度地图中的位置点
   */
  @RequestMapping(value = "/editPosition", method = RequestMethod.GET)
  public String editPosition(Long id, ModelMap model) {
    Seller seller = sellerService.find(id);
    model.addAttribute("seller", seller);
    return "/seller/editPosition";
  }

  /**
   * 数据导出
   */
  @RequestMapping(value = "/dataExport", method = {RequestMethod.GET, RequestMethod.POST})
  public void dataExport(SellerRequest request, HttpServletResponse response) {
    List<Filter> filters = new ArrayList<Filter>();
    filters.add(Filter.ne("accountStatus", AccountStatus.DELETE));
    if (StringUtils.isNotEmpty(request.getName())) {
      filters.add(Filter.like("name", request.getName()));
    }
    if (request.getIsBeanPay() != null) {
      filters.add(Filter.eq("isBeanPay", request.getIsBeanPay()));
    }
    if (StringUtils.isNotEmpty(request.getLicenseNum())) {
      filters.add(Filter.like("licenseNum", request.getLicenseNum()));
    }
    if (StringUtils.isNotEmpty(request.getContactCellPhone())) {
      filters.add(Filter.like("contactCellPhone", request.getContactCellPhone()));
    }
    if (StringUtils.isNotEmpty(request.getCellPhoneNum())) {
      filters.add(Filter.like("endUser&cellPhoneNum", "%" + request.getCellPhoneNum() + "%"));
    }
    if (StringUtils.isNotEmpty(request.getContactPerson())) {
      filters.add(Filter.like("contactPerson", request.getContactPerson()));
    }
    if (request.getAreaId() != null) {
      filters.add(Filter.eq("area", request.getAreaId()));
    }
    if (request.getSellerCategoryId() != null) {
      filters.add(Filter.eq("sellerCategory", request.getSellerCategoryId()));
    }
    if (request.getAccountStatus() != null) {
      filters.add(Filter.eq("accountStatus", request.getAccountStatus()));
    }
    if (request.getApplyFromDate() != null) {
      filters.add(Filter.ge("createDate", TimeUtils.formatDate2Day(request.getApplyFromDate())));
    }
    if (request.getApplyToDate() != null) {
      filters.add(Filter.le("createDate",
          TimeUtils.addDays(1, TimeUtils.formatDate2Day(request.getApplyToDate()))));
    }
    List<Seller> lists = sellerService.findList(null, filters, null);
    if (lists != null && lists.size() > 0) {
      String title = "Seller List"; // 工作簿标题，同时也是excel文件名前缀
      String[] headers =
          {"sellerId", "sellerName", "sellerCategory", "contactPerson", "contactCellPhone",
              "endUserCellPhone", "endUserNickName", "area", "address", "licenseNum", "rateScore",
              "avgPrice", "discount", "limitAmountByDay", "businessTime", "favoriteNum",
              "totalOrderNum", "accountStatus", "isBeanPay", "description"}; // 需要导出的字段
      String[] headersName =
          {"商家编号", "商家名字", "商家类别", "商家联系人", "商家联系人手机", "申请人(用户)手机号", "用户昵称", "地区", "商家地址", "营业执照号",
              "评分", "均价", "折扣", "每日营业额上限", "营业时间", "商家被收藏数", "总订单数", "商家状态", "支持乐豆抵扣", "店铺介绍"}; // 字段对应列的列名
      List<Map<String, String>> mapList = ExportUtils.prepareExportSeller(lists);
      if (mapList.size() > 0) {
        exportListToExcel(response, mapList, title, headers, headersName);
      }
    }
  }
  /**
   * 下载商家二维码图片
   * 
   */
  @RequestMapping(value = "/downloadQRCoder", method = {RequestMethod.GET, RequestMethod.POST})
  protected void downloadQRCoder(Long sellerID, ImageSize imageSize, QrCodeType qrCodeType, 
		  HttpServletResponse response, HttpSession session) {
	  
	  Seller seller = sellerService.find(sellerID);
	  if (seller == null){
		  LogUtil.debug(this.getClass(), "downloadQRCoder", "seller is null");
		  return;
	  }
	  
	  String logoImageURL = null;
	  String content = null;
	  if (qrCodeType == QrCodeType.SHARE) {
		  logoImageURL = session.getServletContext().getRealPath("/") + 
		  			"resources" + File.separator + "images" + File.separator + "system_logo.png"; 
		  if (setting.getRecommendUrl() != null && seller.getEndUser() != null) {
			  content = setting.getRecommendUrl() + "?cellPhoneNum=" + seller.getEndUser().getCellPhoneNum();
		  }
	  }else if (qrCodeType == QrCodeType.PAID) {
		  if (seller.getStorePictureUrl() != null) {
			  logoImageURL = sellerService.getDiskPath(seller.getStorePictureUrl());
		  }
		  content = "{\"flag\":\"" + DigestUtils.md5Hex("翼享生活") + "\",\"sellerId\":\"" 
				  + seller.getId() + "\"}";
	  }
	  if (logoImageURL == null || content == null){
		  LogUtil.debug(this.getClass(), "downloadQRCoder", "logoImageURL or content is null");
		  return;
	  }
	  
	  Integer size = 1;
	  if (imageSize == ImageSize.MIDDLE)  size = 2;
	  if (imageSize == ImageSize.BIG) size = 4;
	  
      try {
        response.setContentType("octets/stream");
        String filename = seller.getContactCellPhone() 
        		+ "_" + qrCodeType.toString() + "_" + imageSize.toString() + "_" 
        		+ DateUtils.getDateFormatString(DateUtils.filePostfixFormat, new Date());
        response.addHeader("Content-Disposition", "attachment;filename=" + filename + ".jpg");
        
        OutputStream out = response.getOutputStream();// 获得输出流
        QRCodeGenerator generator = new QRCodeGenerator(content, logoImageURL, size, out);
        Object locker = new Object();//当前主线程的一把锁
        synchronized (locker) {
          threadPoolExecutor.execute(//加入到线程池中执行
        	new Runnable() {
              public void run() {
            	  generator.generateQrImage();
            	  synchronized (locker) {
            	      locker.notify();
            	  }
              }
            });
          locker.wait();//主线程等待
        }

        out.flush();
        out.close();

      } catch (Exception e) {
        e.printStackTrace();
      }

  }
}

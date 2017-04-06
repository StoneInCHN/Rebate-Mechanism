package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.beans.CommonAttributes;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.TopBanner;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.ordering.Ordering.Direction;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.request.UserRequest;
import org.rebate.service.TopBannerService;
import org.rebate.utils.FieldFilterUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - Banner
 * 
 *
 */
@Controller("bannerController")
@RequestMapping("/banner")
public class BannerController extends MobileBaseController {

  @Resource(name = "topBannerServiceImpl")
  private TopBannerService topBannerService;



  /**
   * 首页顶部广告
   * 
   * @return
   */
  @RequestMapping(value = "/hpTop", method = RequestMethod.POST)
  public @ResponseBody ResponseMultiple<Map<String, Object>> selectArea(
      @RequestBody UserRequest request) {

    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

    List<Filter> filters = new ArrayList<Filter>();
    Filter filter = new Filter("isActive", Operator.eq, true);
    filters.add(filter);

    List<Ordering> orders = new ArrayList<Ordering>();
    Ordering ordering = new Ordering("bannerOrder", Direction.asc);
    orders.add(ordering);

    List<TopBanner> topBanners = topBannerService.findList(null, filters, orders);
    String[] propertys = {"id", "bannerName", "bannerUrl", "linkUrl"};
    List<Map<String, Object>> result = FieldFilterUtils.filterCollectionMap(propertys, topBanners);

    response.setMsg(result);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

}

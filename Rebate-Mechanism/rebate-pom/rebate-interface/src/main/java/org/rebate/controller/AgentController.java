package org.rebate.controller;

import javax.annotation.Resource;

import org.rebate.aspect.UserParam.CheckUserType;
import org.rebate.aspect.UserValidCheck;
import org.rebate.beans.CommonAttributes;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.Area;
import org.rebate.entity.commonenum.CommonEnum.AgencyLevel;
import org.rebate.json.base.PageResponse;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.request.AgentAreaRequest;
import org.rebate.json.response.AreaAmountResponse;
import org.rebate.json.response.AreaCountResponse;
import org.rebate.json.response.SalesManReport;
import org.rebate.service.AreaConsumeReportService;
import org.rebate.service.AreaService;
import org.rebate.service.EndUserService;
import org.rebate.utils.TokenGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - Agent
 * 
 *
 */
@Controller("agentController")
@RequestMapping("/agent")
public class AgentController extends MobileBaseController {

  @Resource(name = "areaConsumeReportServiceImpl")
  private AreaConsumeReportService areaConsumeReportService;
  
  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;
  
  @Resource(name = "areaServiceImpl")
  private AreaService areaService;

  /**
   * 地区交易额统计
   * 
   * @return
   */
  @RequestMapping(value = "/consumeAmountReport", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseMultiple<AreaAmountResponse> consumeAmountReport(
      @RequestBody AgentAreaRequest request) {

    ResponseMultiple<AreaAmountResponse> response = new ResponseMultiple<AreaAmountResponse>();
    
    Long userId = request.getUserId();
    String token = request.getToken();
    Long areaId = request.getAreaId();
    
    //地区ID不能为空
    if (areaId == null ) {
      response.setCode(CommonAttributes.MISSING_REQUIRE_PARAM);
      response.setDesc(message("rebate.request.param.missing"));
      return response;
    }
    //如果客户端没有传 地区代理级别 的值
	if (request.getAgencyLevel() == null) {
		Area area = areaService.find(request.getAreaId());
		request.setAgencyLevel(getAgencyLevel(area));
	} 
	if (request.getAgencyLevel() == null) {
	   response.setCode(CommonAttributes.FAIL_COMMON);
	   response.setDesc("不能获取地区所属代理级别");
	   return response;
	}
	//分页信息
	if (request.getPageNumber() == null) {
		request.setPageNumber(1);//默认第一页
	}
	if (request.getPageSize() == null) {
		request.setPageSize(CommonAttributes.PAGE_SIZE);//默认每页10条数据
	}	
	PageResponse pageResponse = new PageResponse();
	pageResponse.setPageNumber(request.getPageNumber());
	pageResponse.setPageSize(request.getPageSize());
	response.setPage(pageResponse);
	
    response = areaConsumeReportService.getConsumeAmountReport(request, response);

    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }
  
  /**
   * 地区商家数统计
   * 
   * @return
   */
  @RequestMapping(value = "/sellerCountReport", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseMultiple<AreaCountResponse> sellerCountReport(
      @RequestBody AgentAreaRequest request) {
	  ResponseMultiple<AreaCountResponse> response = new ResponseMultiple<AreaCountResponse>();
	    Long userId = request.getUserId();
	    String token = request.getToken();
	    Long areaId = request.getAreaId();
	    
	    //地区ID不能为空
	    if (areaId == null ) {
	      response.setCode(CommonAttributes.MISSING_REQUIRE_PARAM);
	      response.setDesc(message("rebate.request.param.missing"));
	      return response;
	    }
	    //如果客户端没有传 地区代理级别 的值
		if (request.getAgencyLevel() == null) {
			Area area = areaService.find(request.getAreaId());
			request.setAgencyLevel(getAgencyLevel(area));
		} 
		if (request.getAgencyLevel() == null) {
		   response.setCode(CommonAttributes.FAIL_COMMON);
		   response.setDesc("不能获取地区所属代理级别");
		   return response;
		}
		//分页信息
		if (request.getPageNumber() == null) {
			request.setPageNumber(1);//默认第一页
		}
		if (request.getPageSize() == null) {
			request.setPageSize(CommonAttributes.PAGE_SIZE);//默认每页10条数据
		}	
		PageResponse pageResponse = new PageResponse();
		pageResponse.setPageNumber(request.getPageNumber());
		pageResponse.setPageSize(request.getPageSize());
		response.setPage(pageResponse);
		
		response = areaConsumeReportService.getSellerCountReport(request, response);
		
		String newtoken = TokenGenerator.generateToken(token);
		endUserService.createEndUserToken(newtoken, userId);
		response.setToken(newtoken);
		response.setCode(CommonAttributes.SUCCESS);
		return response;
  }
  /**
   * 地区消费者数统计
   * 
   * @return
   */
  @RequestMapping(value = "/endUserCountReport", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseMultiple<AreaCountResponse> endUserCountReport(
      @RequestBody AgentAreaRequest request) {
	  ResponseMultiple<AreaCountResponse> response = new ResponseMultiple<AreaCountResponse>();
	    Long userId = request.getUserId();
	    String token = request.getToken();
	    Long areaId = request.getAreaId();
	    
	    //地区ID不能为空
	    if (areaId == null ) {
	      response.setCode(CommonAttributes.MISSING_REQUIRE_PARAM);
	      response.setDesc(message("rebate.request.param.missing"));
	      return response;
	    }
	    //如果客户端没有传 地区代理级别 的值
		if (request.getAgencyLevel() == null) {
			Area area = areaService.find(request.getAreaId());
			request.setAgencyLevel(getAgencyLevel(area));
		} 
		if (request.getAgencyLevel() == null) {
		   response.setCode(CommonAttributes.FAIL_COMMON);
		   response.setDesc("不能获取地区所属代理级别");
		   return response;
		}
		//分页信息
		if (request.getPageNumber() == null) {
			request.setPageNumber(1);//默认第一页
		}
		if (request.getPageSize() == null) {
			request.setPageSize(CommonAttributes.PAGE_SIZE);//默认每页10条数据
		}	
		PageResponse pageResponse = new PageResponse();
		pageResponse.setPageNumber(request.getPageNumber());
		pageResponse.setPageSize(request.getPageSize());
		response.setPage(pageResponse);
		
		response = areaConsumeReportService.getEndUserCountReport(request, response);
		
		String newtoken = TokenGenerator.generateToken(token);
		endUserService.createEndUserToken(newtoken, userId);
		response.setToken(newtoken);
		response.setCode(CommonAttributes.SUCCESS);
		return response;
  }
  /**
   * 地区业务员数统计
   * 
   * @return
   */
  @RequestMapping(value = "/salesmanCountReport", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseMultiple<AreaCountResponse<SalesManReport>> salesmanCountReport(
      @RequestBody AgentAreaRequest request) {
	  ResponseMultiple<AreaCountResponse<SalesManReport>> response = new ResponseMultiple<AreaCountResponse<SalesManReport>>();
	    Long userId = request.getUserId();
	    String token = request.getToken();
	    Long areaId = request.getAreaId();
	    
	    //地区ID不能为空
	    if (areaId == null ) {
	      response.setCode(CommonAttributes.MISSING_REQUIRE_PARAM);
	      response.setDesc(message("rebate.request.param.missing"));
	      return response;
	    }
	    //如果客户端没有传 地区代理级别 的值
		if (request.getAgencyLevel() == null) {
			Area area = areaService.find(request.getAreaId());
			request.setAgencyLevel(getAgencyLevel(area));
		} 
		if (request.getAgencyLevel() == null) {
		   response.setCode(CommonAttributes.FAIL_COMMON);
		   response.setDesc("不能获取地区所属代理级别");
		   return response;
		}
		//分页信息
		if (request.getPageNumber() == null) {
			request.setPageNumber(1);//默认第一页
		}
		if (request.getPageSize() == null) {
			request.setPageSize(CommonAttributes.PAGE_SIZE);//默认每页10条数据
		}	
		PageResponse pageResponse = new PageResponse();
		pageResponse.setPageNumber(request.getPageNumber());
		pageResponse.setPageSize(request.getPageSize());
		response.setPage(pageResponse);
		
		response = areaConsumeReportService.getSalesmanCountReport(request, response);
		
		String newtoken = TokenGenerator.generateToken(token);
		endUserService.createEndUserToken(newtoken, userId);
		response.setToken(newtoken);
		response.setCode(CommonAttributes.SUCCESS);
		return response;
  }
  /**
   * 根据地区获取代理级别
   * @param area
   * @return
   */
  private AgencyLevel getAgencyLevel(Area area){
	  	AgencyLevel agencyLevel = null;
	  	if (area == null || !area.getTreePath().contains(Area.TREE_PATH_SEPARATOR)) {
	  		return agencyLevel;
	  	}
		int level = area.getTreePath().split(Area.TREE_PATH_SEPARATOR).length;
		if (level == 3) {//县区
			agencyLevel = AgencyLevel.COUNTY;
		}
		if (level == 2) {//市
			agencyLevel = AgencyLevel.CITY;
		} 
		if (level == 0) {//省
			agencyLevel = AgencyLevel.PROVINCE;
		} 
		return agencyLevel;
  }

}

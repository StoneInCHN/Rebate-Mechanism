package org.rebate.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.rebate.controller.base.BaseController;
import org.rebate.entity.ClearingOrderRelation;
import org.rebate.entity.Order;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerClearingRecord;
import org.rebate.entity.SystemWithdrawRecord;
import org.rebate.entity.commonenum.CommonEnum.ClearingStatus;
import org.rebate.framework.filter.Filter;
import org.rebate.service.ClearingOrderRelationService;
import org.rebate.service.OrderService;
import org.rebate.service.SellerClearingRecordService;
import org.rebate.service.SellerService;
import org.rebate.service.SystemWithdrawRecordService;
import org.rebate.utils.HttpServletRequestUtils;
import org.rebate.utils.LogUtil;
import org.rebate.utils.jiupai.pojo.capBatchCollection.BatchCollectionInfo;
import org.rebate.utils.jiupai.pojo.capBatchCollection.BatchCollectionReq;
import org.rebate.utils.jiupai.pojo.capBatchQuery.BatchQueryReq;
import org.rebate.utils.jiupai.pojo.capBatchTransfer.BatchTransferInfo;
import org.rebate.utils.jiupai.pojo.capBatchTransfer.BatchTransferReq;
import org.rebate.utils.jiupai.pojo.capOrderQueryReq.OrderQueryReq;
import org.rebate.utils.jiupai.pojo.capSingleTransfer.SingleTransferReq;
import org.rebate.utils.jiupai.service.GateWayService;
import org.rebate.utils.jiupai.tools.RSASignUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 九派支付接口
 *
 */
@Controller("JiuPaiController")
@RequestMapping("/console/jiupai")
public class JiuPaiController extends BaseController {
	
  @Resource(name = "sellerClearingRecordServiceImpl")
  private SellerClearingRecordService sellerClearingRecordService;
  
  @Resource(name="sellerServiceImpl")
  private SellerService sellerService;
  
  @Resource(name="clearingOrderRelationServiceImpl")
  private ClearingOrderRelationService clearingOrderRelationService;
  
  @Resource(name="orderServiceImpl")
  private OrderService orderService;
  
  @Resource(name="systemWithdrawRecordServiceImpl")
  private SystemWithdrawRecordService systemWithdrawRecordService;
  
  /**
   * 单笔代付
   * @return
   */
  @RequestMapping(value = "/capSingleTransfer", method = RequestMethod.POST)
  public @ResponseBody Map<String, String> capSingleTransfer() {
      GateWayService gateWayService = new GateWayService();
      SingleTransferReq req = new SingleTransferReq();
      req.setAmount("1");//交易金额  单位：分
      req.setCardNo("6231560200156413");//银行卡号
      req.setAccName("张三");//账户户名
      req.setRemark("demo测试");//订单备注
      req.setCallBackUrl("/console/jiupai/notify.jhtml");//回调地址URL
      return gateWayService.capSingleTransfer(req, "201706071818");
  }
  /**
   * 批量代付
   * @return
   */
  @RequestMapping(value = "/capBatchTransfer", method = RequestMethod.POST)
  public @ResponseBody Map<String, String> capBatchTransfer() {
      GateWayService gateWayService = new GateWayService();
      BatchTransferReq req = new BatchTransferReq();
      List<BatchTransferInfo> infoList = new ArrayList<BatchTransferInfo>();
      for (int i = 0; i < 2; i++) {
    	  BatchTransferInfo info = new BatchTransferInfo();
 	     info.setTxnAmt("1");//交易金额 分
 	     info.setCardNo("6231560200156413");
 	     info.setUsrNm("张三");
 	     String withDrawSn = "201706071717";//货款单号
 	     info.setMercOrdNo(withDrawSn);
 	     info.setRmk("批量代付-demo测试"+i);
 	     infoList.add(info);
	  }
      req.setCount(infoList.size());
      req.setInfoList(infoList);
      return gateWayService.capBatchTransfer(req);
  }
  /**
   * 批量代收付查询
   * @return
   */
  @RequestMapping(value = "/capBatchQuery", method = RequestMethod.POST)
  public @ResponseBody Map<String, String> capBatchQuery() {
      GateWayService gateWayService = new GateWayService();
	  BatchQueryReq req = new BatchQueryReq();
	  req.setBatchNo("20170819225505");
	  req.setPageNum(1);
	  req.setPageSize(200);
      return gateWayService.capBatchQuery(req);
  } 
  /**
   * 订单查询
   * @return
   */
  @RequestMapping(value = "/capOrderQuery", method = RequestMethod.POST)
  public @ResponseBody Map<String, String> capOrderQuery() {
      GateWayService gateWayService = new GateWayService();
      OrderQueryReq req = new OrderQueryReq("8000023085100011503370039995", "20170822104719", "800002308510001201708214446", "32080");
      return gateWayService.capOrderQuery(req);
  }
  /**
   * 批量代收
   * @return
   */
  @RequestMapping(value = "/capBatchCollection", method = RequestMethod.POST)
  public @ResponseBody Map<String, String> capBatchCollection() {
      GateWayService gateWayService = new GateWayService();
      BatchCollectionReq req = new BatchCollectionReq();
      List<BatchCollectionInfo> infoList = new ArrayList<BatchCollectionInfo>();
      for (int i = 0; i < 2; i++) {
 	     BatchCollectionInfo info = new BatchCollectionInfo();
 	     info.setTxnAmt("1");//交易金额 分
 	     info.setCardNo("6231560200156413");
 	     info.setUsrNm("张三");
 	     info.setIdNo("510124198554875219");//身份证号
 	     info.setPhoneNo("15625563378");//银行预留手机号
 	     info.setRmk("批量代收-demo测试"+i);
 	     infoList.add(info);
	  }
      req.setCount(infoList.size());
      req.setInfoList(infoList);
	  return gateWayService.capBatchCollection(req);
  }
  /**
   * (九派渠道)单笔货款结算 回调接口
   * 
   * @param req
   * @return
   * @throws IOException
   */
  @RequestMapping(value = "/notifyClearingRecord", method = RequestMethod.POST)
  public @ResponseBody String notifyClearingRecord(HttpServletRequest request) throws Exception {
	LogUtil.debug(this.getClass(), "notifyClearingRecord", "进入 (九派回调  单笔结算) 接口");
    //获取九派反馈信息
	request.setCharacterEncoding("GB18030");//九派默认回调请求参数 编码格式 GB18030
	String responseStr = HttpServletRequestUtils.getRequestParam(request, "GB18030");
	LogUtil.debug(this.getClass(), "notifyClearingRecord", "(九派回调  单笔结算) responseStr:" + responseStr);
	//String merchantId = new String(request.getParameter("merchantId").getBytes("ISO-8859-1"), "GB18030");
	Map<String, String> resMap = null;
	if (StringUtils.isNotBlank(responseStr) && responseStr.indexOf("orderSts") >= 0) {
		RSASignUtil resUtil = new RSASignUtil();
		resMap = resUtil.coverString2Map(responseStr);
	}else {
		return "result=FAILED";
	}
	if (resMap == null) {
		return "result=FAILED";
	}
	String charset = resMap.get("charset");
	String merchantId = resMap.get("merchantId");
	String orderSts = resMap.get("orderSts");
	String orderNo = resMap.get("orderNo");
	LogUtil.debug(this.getClass(), "notifyClearingRecord", "(九派回调  单笔结算) charset:%s, merchantId:%s, orderSts:%s, orderNo:%s", 
			charset, merchantId, orderSts, orderNo);
	String clearingSn = null;
	if (orderNo.startsWith(merchantId)) {
		clearingSn = orderNo.replace(merchantId, "");//merchantId  +  withDrawSn = 订单号
		LogUtil.debug(this.getClass(), "notifyClearingRecord", "(九派回调  单笔结算)  clearingSn:%s", clearingSn);
		SellerClearingRecord record = null;
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(Filter.eq("clearingSn", clearingSn));//结算货款单编号
		filters.add(Filter.eq("clearingStatus", ClearingStatus.PROCESSING));//九派只回调 单笔代付响应  S交易成功 的代付，之前暂时设置为处理中,货款未结算
		filters.add(Filter.eq("isClearing", false));//未结算
		List<SellerClearingRecord> records = sellerClearingRecordService.findList(null, filters, null);
		if (records != null && records.size() > 0) {
			  if (records.size() > 1) {
				  LogUtil.debug(this.getClass(), "notifyClearingRecord", "(九派回调  单笔结算) 找到多个商家货款记录");//这种情况不应该出现
			  }
			  record = records.get(0);
			  if ("S".equals(orderSts)) {//处理成功
	      		  	//将货款的结算状态改为处理成功即结算成功，是否结算改为true
	      	    	record.setClearingStatus(ClearingStatus.SUCCESS);
	      	    	record.setIsClearing(true);
	      	    	record.setRemark(record.getRemark() + ";回调通知:处理成功");
	      	    	sellerClearingRecordService.update(record);
	      	    	//结算成功后，将商家的未结算货款金额减去本次货款已结算金额
	      	    	if (record.getSeller() != null && record.getSeller().getId() != null) {
	      	    		Seller seller = sellerService.find(record.getSeller().getId());
	      	    		seller.setUnClearingAmount(seller.getUnClearingAmount().subtract(record.getAmount()));
	      	    		sellerService.update(seller);
	      	    	}
	      	    	//将本次货款涉及到的所有订单是否结算状态改为true即已结算
	      			List<Filter> filtersRelation = new ArrayList<Filter>();
	      			filters.add(Filter.eq("clearingRecId", record.getId()));
	      			List<ClearingOrderRelation> relations = clearingOrderRelationService.findList(null, filtersRelation, null);
	      			List<Order> orders = new ArrayList<Order>();
	      			for (ClearingOrderRelation relation : relations) {
	      				if (relation != null && relation.getOrder() != null && relation.getOrder().getId() != null) {
	      					Order order = orderService.find(relation.getOrder().getId());//不明白为啥直接用relation.getOrder()要报错-->could not initialize proxy - no Session
	      					order.setIsClearing(true);
	      					orders.add(order);
	      				}
	      			}
	            	orderService.update(orders);
	            	return "result=SUCCESS";
			  }
        	  if ("F".equals(orderSts) || "R".equals(orderSts)) {//处理失败 
        		  	//将货款的结算状态改为处理失败即结算失败，是否结算改为false
        	    	record.setClearingStatus(ClearingStatus.FAILED);
        	    	record.setIsClearing(false);
        	    	record.setRemark(record.getRemark() + ";回调通知:处理失败 ");
        	    	sellerClearingRecordService.update(record);
        	    	return "result=SUCCESS";
			  }
		}else {
			  LogUtil.debug(this.getClass(), "notifyClearingRecord", "(九派回调  单笔结算)未找到处理中记录");
		}
	}
    return "result=FAILED";
  }
  /**
   * (九派渠道)平台提现 回调接口
   * 
   * @param req
   * @return
   * @throws IOException
   */
  @RequestMapping(value = "/notifySystemWithdraw", method = RequestMethod.POST)
  public @ResponseBody String notifySystemWithdraw(HttpServletRequest request) throws Exception {
	  LogUtil.debug(this.getClass(), "notifyClearingRecord", "进入 (九派回调  平台提现) 接口");
    //获取九派反馈信息
	request.setCharacterEncoding("GB18030");//九派默认回调请求参数 编码格式 GB18030
	String responseStr = HttpServletRequestUtils.getRequestParam(request, "GB18030");
	LogUtil.debug(this.getClass(), "notifySystemWithdraw", "(九派回调  平台提现) responseStr:" + responseStr);
    //String merchantId = new String(request.getParameter("merchantId").getBytes("ISO-8859-1"), "GB18030");
	Map<String, String> resMap = null;
	if (StringUtils.isNotBlank(responseStr) && responseStr.indexOf("orderSts") >= 0) {
		RSASignUtil resUtil = new RSASignUtil();
		resMap = resUtil.coverString2Map(responseStr);
	}else {
		return "result=FAILED";
	}
	if (resMap == null) {
		return "result=FAILED";
	}
	String charset = resMap.get("charset");
	String merchantId = resMap.get("merchantId");
	String orderSts = resMap.get("orderSts");
	String orderNo = resMap.get("orderNo");
	LogUtil.debug(this.getClass(), "notifySystemWithdraw", "(九派回调  平台提现) charset:%s, merchantId:%s, orderSts:%s, orderNo:%s", 
			charset, merchantId, orderSts, orderNo);
	if (orderNo.startsWith(merchantId)) {
		SystemWithdrawRecord record = null;
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(Filter.eq("reqSn", orderNo));//交易批次号
		filters.add(Filter.eq("status", ClearingStatus.PROCESSING));//处理中
		filters.add(Filter.eq("isWithdraw", false));//未提现
		List<SystemWithdrawRecord> records = systemWithdrawRecordService.findList(null, filters, null);
		if (records != null && records.size() > 0) {
			  if (records.size() > 1) {
				  LogUtil.debug(this.getClass(), "notifySystemWithdraw", "(九派回调  平台提现) 找到多个商家货款记录");//这种情况不应该出现
			  }
			  record = records.get(0);
			  if ("S".equals(orderSts)) {//处理成功
	      	    	record.setStatus(ClearingStatus.SUCCESS);
	      	    	record.setIsWithdraw(true);
	      	    	record.setWithdrawMsg(record.getWithdrawMsg() + ";回调通知:处理成功");
	      	    	systemWithdrawRecordService.update(record);
	            	return "result=SUCCESS";
			  }
        	  if ("F".equals(orderSts) || "R".equals(orderSts)) {//处理失败 
	      	    	record.setStatus(ClearingStatus.FAILED);
	      	    	record.setIsWithdraw(true);
	      	    	record.setWithdrawMsg(record.getWithdrawMsg() + ";回调通知:处理失败");
	      	    	systemWithdrawRecordService.update(record);
	            	return "result=SUCCESS";
			  }
		}else {
			  LogUtil.debug(this.getClass(), "notifySystemWithdraw", "(九派回调  平台提现)未找到处理中记录");
		}
	}
    return "result=FAILED";
  }
  
}

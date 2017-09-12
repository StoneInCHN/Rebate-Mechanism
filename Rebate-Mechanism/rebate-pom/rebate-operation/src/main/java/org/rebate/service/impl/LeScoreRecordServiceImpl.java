package org.rebate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.beans.Setting;
import org.rebate.dao.LeScoreRecordDao;
import org.rebate.entity.BankCard;
import org.rebate.entity.EndUser;
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;
import org.rebate.entity.commonenum.CommonEnum.ClearingStatus;
import org.rebate.entity.commonenum.CommonEnum.LeScoreType;
import org.rebate.entity.commonenum.CommonEnum.PaymentChannel;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.BankCardService;
import org.rebate.service.EndUserService;
import org.rebate.service.LeScoreRecordService;
import org.rebate.service.ParamConfigService;
import org.rebate.service.SystemConfigService;
import org.rebate.utils.LogUtil;
import org.rebate.utils.SettingUtils;
import org.rebate.utils.SpringUtils;
import org.rebate.utils.allinpay.service.TranxServiceImpl;
import org.rebate.utils.jiupai.pojo.capBatchTransfer.BatchTransferInfo;
import org.rebate.utils.jiupai.pojo.capBatchTransfer.BatchTransferReq;
import org.rebate.utils.jiupai.service.GateWayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service("leScoreRecordServiceImpl")
public class LeScoreRecordServiceImpl extends BaseServiceImpl<LeScoreRecord, Long> implements LeScoreRecordService {

  @Resource(name = "leScoreRecordDaoImpl")
  private LeScoreRecordDao leScoreRecordDao;

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;
  
  @Resource(name = "bankCardServiceImpl")
  private BankCardService bankCardService;
  
  @Resource(name="systemConfigServiceImpl") 
  private SystemConfigService systemConfigService; 
  
  @Resource(name="paramConfigServiceImpl")
  private ParamConfigService paramConfigService;
  
  private Setting setting = SettingUtils.get();

  @Resource(name = "leScoreRecordDaoImpl")
  public void setBaseDao(LeScoreRecordDao leScoreRecordDao) {
    super.setBaseDao(leScoreRecordDao);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Message auditWithdraw(LeScoreRecord leScoreRecord) {
    Message message = new Message();
    try {
      LogUtil.debug(LeScoreRecordServiceImpl.class, "auditWithdraw", "leScoreRecord = %s",
          leScoreRecord);
      LeScoreRecord temp = this.find(leScoreRecord.getId());
      EndUser endUser = temp.getEndUser();
      if (endUser == null) {
        LogUtil.debug(LeScoreRecordServiceImpl.class, "auditWithdraw", "endUser不能为空");
        return Message.error("参数不全,用户不能为空");
      } else if (leScoreRecord.getWithdrawStatus() == null || temp.getWithdrawStatus() == null) {
        LogUtil.debug(LeScoreRecordServiceImpl.class, "auditWithdraw", "auditWithdraw不能为空");
        return Message.error("审核状态不能为空");
      } else if (!ApplyStatus.AUDIT_WAITING.equals(temp.getWithdrawStatus())) {
        LogUtil.debug(LeScoreRecordServiceImpl.class, "auditWithdraw",
            "auditWithdraw 不是未待审核状态，不需要审核");
        return Message.error("审核状态错误");
      } else if (ApplyStatus.AUDIT_PASSED.equals(leScoreRecord.getWithdrawStatus())) {
    	  message = Message.success("操作成功!");
      } else if (ApplyStatus.AUDIT_FAILED.equals(leScoreRecord.getWithdrawStatus())) {
        // 审核失败 把 乐分还回到用户手中
  		// 当前乐分
  		BigDecimal curLeScore = temp.getAmount();
  		// 激励乐分(包括乐心分红乐分，推荐获得乐分)
  		BigDecimal motivateLeScore = temp.getMotivateLeScore();
  		// 业务员收益乐分
  		BigDecimal incomeLeScore = temp.getIncomeLeScore();
  		// 代理商提成乐分
  		BigDecimal agentLeScore = temp.getAgentLeScore();
  		if (endUser.getCurLeScore() != null && curLeScore != null) {
  			endUser.setCurLeScore(endUser.getCurLeScore().add(curLeScore.abs()));
			}
  		if (endUser.getMotivateLeScore() != null && motivateLeScore != null) {
  			endUser.setMotivateLeScore(endUser.getMotivateLeScore().add(motivateLeScore.abs()));
			}
  		if (endUser.getAgentLeScore() != null && agentLeScore != null) {
  			endUser.setAgentLeScore(endUser.getAgentLeScore().add(agentLeScore.abs()));
			}
  		if (endUser.getIncomeLeScore() != null && incomeLeScore != null) {
  			endUser.setIncomeLeScore(endUser.getIncomeLeScore().add(incomeLeScore.abs()));
		}
        endUserService.update(endUser);
      }
      temp.setWithdrawStatus(leScoreRecord.getWithdrawStatus());
      temp.setRemark(leScoreRecord.getRemark());
      this.update(temp);
      
    } catch (Exception e) {
      LogUtil.debug(LeScoreRecordServiceImpl.class, "auditWithdraw", "提现审核执行失败 ，异常信息： %s",
          e.getMessage());
      return Message.error("提现审核执行失败 ，异常信息：", e.getMessage());
    }
    return message;
  }
  /**
   * (通联渠道)批量提现
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public String batchWithdrawalByAllinpay(Long[] ids) {

	List<LeScoreRecord> records = new ArrayList<LeScoreRecord>();
	BigDecimal totalClearingAmount = new BigDecimal(0);//乐分提现总金额
	BigDecimal totalHandlingCharge = new BigDecimal(0);//手续费总金额
    for (Long id : ids) {
    	  LeScoreRecord record = leScoreRecordDao.find(id);
    	  if (record != null && record.getWithdrawStatus() == ApplyStatus.AUDIT_PASSED && 
    	      (record.getIsWithdraw() == null || record.getIsWithdraw() == false)) {//未提现并且审核通过的乐分记录(通联)
    	      //&& PaymentChannel.ALLINPAY == record.getPaymentChannel()) {//未提现并且审核通过的乐分记录(通联)
    		//如果乐分记录里面没有记录银行卡，那就用用户默认的银行
			if (record.getWithDrawType() == null && record.getEndUser() != null) {
				BankCard defaultBankCard = bankCardService.getDefaultCard(record.getEndUser());
				if (defaultBankCard != null) {
					record.setWithDrawType(defaultBankCard.getId());
				}
			}
			if (record.getWithDrawType() == null) {
				LogUtil.debug(this.getClass(), "batchWithdrawalByAllinpay", "(通联渠道)乐分提现记录Id:%s, 未找到提现银行卡!",record.getId());
				record.setWithdrawMsg("未找到提现银行卡！");
				update(record);
			}else if(record.getAmount() != null) {
				BigDecimal leAmount = record.getAmount().abs();
				leAmount = leAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
				totalClearingAmount = totalClearingAmount.add(leAmount);
				BigDecimal handingCharge = getAllinpayHandlingCharge(leAmount);
 				if (handingCharge != null) {//代付金额 应该是扣除手续费的 乐分金额
  					 BigDecimal payAmount = leAmount.subtract(handingCharge);
  					 if (payAmount.subtract(new BigDecimal(0.01)).signum() <= 0) {
  						 LogUtil.debug(this.getClass(), "batchWithdrawalByAllinpay", "(通联渠道)提现金额: %s 小于 手续费金额: %s !!!", record.getAmount().abs(), handingCharge);
  						 record.setWithdrawMsg("提现金额不够支付手续费！");
  						 update(record);
  						 continue;
  					 }
  				}
    			
                BankCard defaultCard = bankCardService.find(record.getWithDrawType());//获取商家提现银行卡
                record.setOwnerName(defaultCard.getOwnerName());//持卡人姓名
                record.setCardNum(defaultCard.getCardNum());//银行卡号
                
 				record.setHandlingCharge(handingCharge);//手续费
 				record.setIsWithdraw(false);//暂时先标记为未提现
 				record.setStatus(ClearingStatus.PROCESSING);//处理中...
 				record.setWithdrawMsg("处理中...");//处理中...
 				record.setPaymentChannel(PaymentChannel.ALLINPAY);//(通联渠道)
 				totalHandlingCharge = totalHandlingCharge.add(record.getHandlingCharge()); //累加手续费
				records.add(record);
			}
		}
    }
    if (records.size() == 0) {
    	LogUtil.debug(this.getClass(), "batchWithdrawalByAllinpay", "(通联渠道)没有符合批量提现的记录");
    	return null;
	}

    TranxServiceImpl tranxService = new TranxServiceImpl();
    try {
    	BigDecimal totalPay = totalClearingAmount.subtract(totalHandlingCharge);//总共代付的金额
    	String totalPayStr = totalPay.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString();//金额单位：分
    	String totalItem = records.size() + "";//单数量
    	//开始批量提现（代付）
    	List<LeScoreRecord> recordList = tranxService.batchWithdrawalLeScore(false, totalItem,  totalPayStr, records);
    	if (recordList != null && recordList.size() > 0) {
    		//更新已经受理的乐分提现记录（已赋值了req_sn和sn）
	    	update(recordList);
	        //批量代付（提现）的总交易批次号
	        String reqSn = recordList.get(0).getReqSn();
	        //隔一段时间请求交易结果查询接口，去更新商家货款记录的状态
	        if (reqSn != null) {
	        	return reqSn;
	        }
    	}else {
    		LogUtil.debug(this.getClass(), "batchWithdrawalByAllinpay", "Batch Withdrawal failed, recordList is null or size=0");
    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚当前的事务
    		return null;
		}
	} catch (Exception e) {
		e.printStackTrace();
		LogUtil.debug(this.getClass(), "batchWithdrawalByAllinpay", "Batch Withdrawal failed, Catch exception: %s", e.getMessage());
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚当前的事务
	}
    
    return null;
  }
  /**
   * (九派渠道)批量提现
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public String batchWithdrawalByJiuPai(Long[] ids){
		List<LeScoreRecord> records = new ArrayList<LeScoreRecord>();
	    for (Long id : ids) {
	    	  LeScoreRecord record = leScoreRecordDao.find(id);
	    	  if (record != null && record.getWithdrawStatus() == ApplyStatus.AUDIT_PASSED && 
	    	      (record.getIsWithdraw() == null || record.getIsWithdraw() == false)) {//未提现并且审核通过的乐分记录(九派) 
	    	      //&& PaymentChannel.JIUPAI == record.getPaymentChannel()) {//未提现并且审核通过的乐分记录(九派)
	    		//如果乐分记录里面没有记录银行卡，那就用用户默认的银行
				if (record.getWithDrawType() == null && record.getEndUser() != null) {
					BankCard defaultBankCard = bankCardService.getDefaultCard(record.getEndUser());
					if (defaultBankCard != null) {
						record.setWithDrawType(defaultBankCard.getId());
					}
				}
				if (record.getWithDrawType() == null) {
					LogUtil.debug(this.getClass(), "batchWithdrawalByJiuPai", "(九派渠道)乐分提现记录Id:%s, 未找到提现银行卡!",record.getId());
					record.setWithdrawMsg("未找到提现银行卡！");
					update(record);
				}else if(record.getAmount() != null) {
					BigDecimal leAmount = record.getAmount().abs();
					leAmount = leAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
					BigDecimal handingCharge = getAllinpayHandlingCharge(leAmount);
	 				if (handingCharge != null) {//代付金额 应该是扣除手续费的 乐分金额
	  					 BigDecimal payAmount = leAmount.subtract(handingCharge);
	  					 if (payAmount.subtract(new BigDecimal(0.01)).signum() <= 0) {
	  						 LogUtil.debug(this.getClass(), "batchWithdrawalByJiuPai", "(九派渠道)提现金额: %s 小于 手续费金额: %s !!!", record.getAmount().abs(), handingCharge);
	  						 record.setWithdrawMsg("提现金额不够支付手续费！");
	  						 update(record);
	  						 continue;
	  					 }
	  				}
	 				record.setHandlingCharge(handingCharge);//手续费
	 				record.setIsWithdraw(false);//暂时先标记为未提现
	 				record.setStatus(ClearingStatus.PROCESSING);//处理中...
	 				record.setWithdrawMsg("处理中...");//处理中...
	 				record.setPaymentChannel(PaymentChannel.JIUPAI);//(九派渠道)
					records.add(record);
				}
			}
	    }
	    if (records.size() == 0) {
	    	LogUtil.debug(this.getClass(), "batchWithdrawalByJiuPai", "(九派渠道)没有符合批量提现的记录");
	    	return null;
		}else {
    		Map<String, LeScoreRecord> snWithdrawRecordMap = new HashMap<String, LeScoreRecord>();//乐分提现编号(KEY) : 乐分提现记录 (VALUE)
    	 	for (LeScoreRecord record: records) {
    	 		snWithdrawRecordMap.put(record.getWithDrawSn(), record);
    		}
    	 	//组装请求参数
    	    BatchTransferReq req = new BatchTransferReq();
    	    List<BatchTransferInfo> reqInfoList = new ArrayList<BatchTransferInfo>();
    	    for (Map.Entry<String, LeScoreRecord> entry : snWithdrawRecordMap.entrySet()) {
    	    	  LeScoreRecord record = entry.getValue();
    	    	  if (record.getAmount() != null) {
        	    	  BatchTransferInfo info = new BatchTransferInfo();
        	    	  BigDecimal payAmount = record.getAmount().abs().subtract(record.getHandlingCharge());//提现金额 - 手续费 = 代付的金额
        	    	  String payStr = payAmount.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString();//金额单位：分
        	 	      info.setTxnAmt(payStr);//交易(代付)金额  分
        	 	      BankCard bankCard = bankCardService.find(record.getWithDrawType());
        	 	      info.setCardNo(bankCard.getCardNum());
        	 	      info.setUsrNm(bankCard.getOwnerName());
        	 	      info.setMercOrdNo(record.getWithDrawSn());
        	 	      info.setRmk("九派 提现:" + record.getWithDrawSn());
        	 	      reqInfoList.add(info);
				  }
    		}
    	    req.setCount(reqInfoList.size());
    	    req.setInfoList(reqInfoList);
    	    //开始批量代付
			return capBatchTransfer(req, snWithdrawRecordMap);
		}
    }
  	/**
  	 * (九派渠道)批量代付
  	 * @param req
  	 */
    private String capBatchTransfer(BatchTransferReq req, Map<String, LeScoreRecord> snWithdrawRecordMap){
    	//开始批量代付
    	GateWayService gateWayService = new GateWayService();
    	Map<String, String> resMap = gateWayService.capBatchTransfer(req);
    	//获取响应 infoList
    	String resInfoList = resMap.get("infoList");
    	String reqSn = null;
		JSONArray jsonArray = JSON.parseArray(resInfoList);
		if (jsonArray.size() > 0) {
		  	  String merchantId = setting.getJiupaiMerchantId();
			  for (int j = 0; j < jsonArray.size(); j++) {
				  JSONObject jsonObject = jsonArray.getJSONObject(j);
				  String batchNo = jsonObject.getString("batchNo");
				  String jrnNo = jsonObject.getString("jrnNo");
				  String mercOrdNo = jsonObject.getString("mercOrdNo");
				  String errorCode = jsonObject.getString("errorCode");
				  String errorMsg = jsonObject.getString("errorMsg");
				  LogUtil.debug(this.getClass(), "capBatchTransfer", "(九派渠道)批量提现 merchantId: %s, batchNo: %s, jrnNo: %s, mercOrdNo: %s,errorCode: %s, errorMsg: %s",
						merchantId, batchNo, jrnNo, mercOrdNo, errorCode, errorMsg);
				  if (!mercOrdNo.startsWith(merchantId) || !jrnNo.startsWith(batchNo)) {
					  LogUtil.debug(this.getClass(), "capBatchTransfer", "(九派渠道)批量提现 merchantId + withDrawSn != 商户订单号(mercOrdNo) 或   batchNo + sn != 交易流水 (jrnNo)");
					  continue; 
				  }
				  String withDrawSn = mercOrdNo.replace(setting.getJiupaiMerchantId(), "");//乐分提现记录编号
				  String sn = jrnNo.replace(batchNo, "");//记录序号，例如：0001
				  LeScoreRecord record = snWithdrawRecordMap.get(withDrawSn);
				   if (record != null) {
					    if ("CAP00500".equals(errorCode)) {//受理成功CAP00500
					    	record.setReqSn(batchNo);
					    	record.setSn(sn);
					    	record.setWithdrawMsg(errorMsg);
					    	//更新已经受理的乐分提现记录（已赋值了reqSn和sn）
					    	update(record);
						   	if (reqSn == null) {
						   		reqSn = batchNo;
							}
						}else {//例如  CAP00533:不在指定金额区间        CAP00001:数据库操作异常
					    	record.setReqSn(batchNo);
					    	record.setSn(sn);
					  		record.setIsWithdraw(false);
					  		record.setStatus(ClearingStatus.FAILED);
					  		record.setWithdrawMsg(errorMsg);//提现返回消息
					  		update(record);
					  		// 提现失败 把 乐分还回到用户手中
					  		EndUser endUser = null;
					  		if (record.getEndUser().getId() != null) {
					  			endUser = endUserService.find(record.getEndUser().getId());
								}
					  		if (endUser != null) {
					  			refundLeScore(endUser, record);//退回乐分
							}
						}
				   }
			   }
		 }
		return reqSn;
    }
    /**
     * 退回乐分给用户，并生成乐分退回记录
     * @param endUser
     * @param record
     */
    public void refundLeScore(EndUser endUser, LeScoreRecord record){
  	    LogUtil.debug(this.getClass(), "refundLeScore", "退回乐分给用户Id:%s",endUser.getId());
    		// 当前乐分
    		BigDecimal curLeScore = record.getAmount();
    		// 激励乐分(包括乐心分红乐分，推荐获得乐分)
    		BigDecimal motivateLeScore = record.getMotivateLeScore();
    		// 业务员收益乐分
    		BigDecimal incomeLeScore = record.getIncomeLeScore();
    		// 代理商提成乐分
    		BigDecimal agentLeScore = record.getAgentLeScore();
    		if (endUser.getCurLeScore() != null && curLeScore != null) {
    			endUser.setCurLeScore(endUser.getCurLeScore().add(curLeScore.abs()));
  		}
    		if (endUser.getMotivateLeScore() != null && motivateLeScore != null) {
    			endUser.setMotivateLeScore(endUser.getMotivateLeScore().add(motivateLeScore.abs()));
  		}
    		if (endUser.getAgentLeScore() != null && agentLeScore != null) {
    			endUser.setAgentLeScore(endUser.getAgentLeScore().add(agentLeScore.abs()));
  		}
    		if (endUser.getIncomeLeScore() != null && incomeLeScore != null) {
    			endUser.setIncomeLeScore(endUser.getIncomeLeScore().add(incomeLeScore.abs()));
  		}
    		endUserService.update(endUser);
      	LeScoreRecord refundRecord = new LeScoreRecord();//乐分退回记录
      	refundRecord.setLeScoreType(LeScoreType.REFUND);
      	refundRecord.setEndUser(endUser);
      	refundRecord.setAmount(record.getAmount() !=null? record.getAmount().abs():null);
      	refundRecord.setMotivateLeScore(record.getMotivateLeScore());
      	refundRecord.setAgentLeScore(record.getAgentLeScore());
      	refundRecord.setIncomeLeScore(record.getIncomeLeScore());
      	refundRecord.setSeller(record.getSeller());
      	refundRecord.setUserCurLeScore(endUser.getCurLeScore());
      	//例如：乐分退回来自提现流水号：2007010000044091500518983865
      	refundRecord.setWithdrawMsg(SpringUtils.getMessage("rebate.endUser.leScore.REFUND.msg", record.getReqSn()));
      	//例如：提现客户付款帐户余额不足乐分退回
      	refundRecord.setRemark(SpringUtils.getMessage("rebate.endUser.leScore.type.WITHDRAW") + record.getWithdrawMsg()
      	    		+SpringUtils.getMessage("rebate.endUser.leScore.type.REFUND"));
      	save(refundRecord);
      	LogUtil.debug(this.getClass(), "refundLeScore", "并生成乐分退回记录Id:%s",refundRecord.getId());
    }
  /**
   * 获取通联货款结算手续费
   * @param totalOrderAmount
   * @return
   */
	private BigDecimal getAllinpayHandlingCharge(BigDecimal totalOrderAmount) {
	    //手续费，优先考虑 每笔提现固定手续费（通联固定每笔是1.5元？？），后考虑提现手续费占提现金额的百分比 
		BigDecimal handlingCharge = new BigDecimal(0);
		try {
			SystemConfig feePertime = systemConfigService.getConfigByKey(SystemConfigKey.TRANSACTION_FEE_PERTIME);
			if (feePertime != null && feePertime.getConfigValue() != null) {
				handlingCharge = new BigDecimal(feePertime.getConfigValue());
			}else {
				SystemConfig feePercentage = systemConfigService.getConfigByKey(SystemConfigKey.TRANSACTION_FEE_PERCENTAGE);
				if (feePercentage != null && feePercentage.getConfigValue() != null) {
			    	handlingCharge = totalOrderAmount.multiply(new BigDecimal(feePercentage.getConfigValue()));
				}
			}
		} catch (Exception e) {
			LogUtil.debug(this.getClass(), "getHandlingCharge", "Catch exception: %s", e.getMessage());
			return handlingCharge;
		}
		return handlingCharge;
	}
	@Override
	public List<String> jiuPaiProcessingBatchNoList(Date endDate) {
		return leScoreRecordDao.jiuPaiProcessingBatchNoList(endDate);
	}
//	/**
//	 * 单笔实时提现（代付）
//	 */
//	@Override
//	public Message singlePay(LeScoreRecord record, BankCard bankCard) {
//	      try {
//			    TranxServiceImpl tranxService = new TranxServiceImpl();
//			    BigDecimal handingCharge = getAllinpayHandlingCharge(record.getAmount());//手续费
//			    BigDecimal payAmount = record.getAmount().abs().subtract(handingCharge);
//			    //因为手续费要在结算金额里面扣除，所以结算金额应该至少多余手续费一分钱
//			    //否者放弃代付此单，同时将其设置为处理失败，标明备注：结算金额不够支付手续费！方便后台手动处理
//			    if (payAmount.subtract(new BigDecimal(0.01)).signum() <= 0) {
//			    	record.setStatus(ClearingStatus.FAILED);
//			    	record.setIsWithdraw(false);
//			    	record.setRemark(SpringUtils.getMessage("rebate.sellerClearingRecord.incomeAmount.less.than.handlingCharge"));
//			    	update(record);
//			    	LogUtil.debug(this.getClass(), "singlePay", "Withdraw Amount: %s is less than Handling Charge: %s !!!", record.getAmount(), handingCharge);
//			    	return Message.success("rebate.sellerClearingRecord.incomeAmount.less.than.handlingCharge");
//			    }
//					 
//			    Map<String, String> resultMap =  tranxService.singleDaiFushi(false, bankCard.getOwnerName(), bankCard.getCardNum(), payAmount.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString());
//			    if (resultMap.containsKey("status") && resultMap.containsKey("req_sn")){
//			    	String status = resultMap.get("status");
//			    	if ("success".equals(status)) {
//			    		record.setStatus(ClearingStatus.SUCCESS);
//				    	record.setIsWithdraw(true);
//					}else if ("error".equals(status)) {
//						record.setStatus(ClearingStatus.FAILED);
//						record.setIsWithdraw(false);
//					}
//			    	record.setReqSn(resultMap.get("req_sn"));
//			    	record.setSn(null);//单笔不像批量，没有sn号
//			    	record.setRemark(resultMap.get("err_msg"));
//			    	update(record);
//				}
//	        } catch (Exception e) {
//	          e.printStackTrace();
//	          return Message.error("rebate.common.system.error");
//	        }
//	        return Message.success("rebate.message.success");
//	}
}

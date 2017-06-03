package org.rebate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.rebate.beans.Message;
import org.rebate.dao.LeScoreRecordDao;
import org.rebate.entity.BankCard;
import org.rebate.entity.EndUser;
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.ParamConfig;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;
import org.rebate.entity.commonenum.CommonEnum.ParamConfigKey;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.BankCardService;
import org.rebate.service.EndUserService;
import org.rebate.service.LeScoreRecordService;
import org.rebate.service.ParamConfigService;
import org.rebate.service.SystemConfigService;
import org.rebate.utils.LogUtil;
import org.rebate.utils.allinpay.service.TranxServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service("leScoreRecordServiceImpl")
public class LeScoreRecordServiceImpl extends BaseServiceImpl<LeScoreRecord, Long> implements
    LeScoreRecordService {

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
          //乐分提现以前是在审核通过后，立即微信单笔提现，现在改成了通联批量提现（代付）,具体参考 batchWithdrawal方法
//        String partner_trade_no =
//            String.valueOf((int) (Math.random() * 10000000) + System.currentTimeMillis()
//                + (int) (Math.random() * 1000000));
//        Integer amount = temp.getAmount().multiply(new BigDecimal(100)).intValue();
//        ResponseOne<Map<String, Object>> response =
//            PayUtil.wechatTransfers(endUser.getWechatOpenid(), amount, "用户提现", partner_trade_no);
//        String code = response.getCode();
//        if (CommonAttributes.SUCCESS.equals(code)) {
//          // 提现成功
//          Map<String, Object> map = response.getMsg();
//          System.out.println(map);
//          temp.setIsWithdraw(true);
//
//        } else {
//          // 提现失败
//          temp.setIsWithdraw(false);
//          temp.setWithdrawMsg(response.getDesc());
//        }
      } else if (ApplyStatus.AUDIT_FAILED.equals(leScoreRecord.getWithdrawStatus())) {
        // 审核失败 把 乐分还回到用户手中
        // 当前乐分
        BigDecimal curLeScore = temp.getAmount();
        // 激励乐分(包括乐心分红乐分，推荐获得乐分)
        BigDecimal motivateLeScore = temp.getMotivateLeScore();
        // 商家直接收益乐分
   //     BigDecimal incomeLeScore = temp.getIncomeLeScore();
        // 代理商提成乐分
        BigDecimal agentLeScore = temp.getAgentLeScore();
        endUser.setCurLeScore(endUser.getCurLeScore().add(curLeScore));
        endUser.setMotivateLeScore(endUser.getMotivateLeScore().add(motivateLeScore));
    //    endUser.setIncomeLeScore(endUser.getIncomeLeScore().add(incomeLeScore));
        endUser.setAgentLeScore(endUser.getAgentLeScore().add(agentLeScore));
        endUserService.update(endUser);
      }
      temp.setWithdrawStatus(leScoreRecord.getWithdrawStatus());
      temp.setRemark(leScoreRecord.getRemark());
      this.update(temp);
      message = Message.success("操作成功!");
    } catch (Exception e) {
      LogUtil.debug(LeScoreRecordServiceImpl.class, "auditWithdraw", "提现审核执行失败 ，异常信息： %s",
          e.getMessage());
      return Message.error("提现审核执行失败 ，异常信息：", e.getMessage());
    }
    return message;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Message batchWithdrawal(Long[] ids) {
	if (ids == null || ids.length == 0) {
		return Message.error("请至少选择一条记录！");
	}
	List<LeScoreRecord> records = new ArrayList<LeScoreRecord>();
	BigDecimal totalClearingAmount = new BigDecimal(0);//乐分提现总金额
	BigDecimal totalHandlingCharge = new BigDecimal(0);//手续费总金额
    for (Long id : ids) {
    	  LeScoreRecord record = leScoreRecordDao.find(id);
    	  if (record != null && record.getIsWithdraw() == false 
    			  && record.getWithdrawStatus() == ApplyStatus.AUDIT_PASSED) {//未提现并且审核通过的乐分记录
    		//如果乐分记录里面没有记录银行卡，那就用用户默认的银行
			if (record.getWithDrawType() == null && record.getEndUser() != null) {
				BankCard defaultBankCard = bankCardService.getDefaultCard(record.getEndUser());
				if (defaultBankCard != null) {
					record.setWithDrawType(defaultBankCard.getId());
				}
			}
			if (record.getWithDrawType() == null) {
				LogUtil.debug(this.getClass(), "batchWithdrawal", "Cannot find withdrawal bankcard for LeScoreRecord(id): %s",record.getId());
				record.setWithdrawMsg("未找到提现银行卡！");
				update(record);
			}else {
				totalClearingAmount = totalClearingAmount.add(record.getAmount());
				BigDecimal handingCharge = getAllinpayHandlingCharge(record.getAmount());
 				if (record.getAmount() != null && handingCharge != null) {
  					 //因为手续费要在提现金额里面扣除，所以提现金额应该至少多余手续费一分钱
  					 //否者放弃提现此单，标明备注：提现金额不够支付手续费！
  					 BigDecimal payAmount = record.getAmount().subtract(handingCharge);
  					 if (payAmount.subtract(new BigDecimal(0.01)).signum() <= 0) {
  						 LogUtil.debug(this.getClass(), "batchWithdrawal", "Withdrawal Amount: %s is less than Handling Charge: %s !!!", record.getAmount(), handingCharge);
  						 record.setWithdrawMsg("提现金额不够支付手续费！");
  						 update(record);
  						 continue;
  					 }
  				}
 				record.setHandlingCharge(handingCharge);//手续费
 				record.setIsWithdraw(true);//暂时先标记为已提现
 				record.setWithdrawMsg("处理中...");//处理中...
 				totalHandlingCharge = totalHandlingCharge.add(record.getHandlingCharge()); //累加手续费
				records.add(record);
			}
		}
    }
    if (records.size() == 0) {
    	return Message.success("没有符合批量提现的记录");
	}

    TranxServiceImpl tranxService = new TranxServiceImpl();
    tranxService.init();//初始化通联基础数据
    try {
    	BigDecimal totalPay = totalClearingAmount.subtract(totalHandlingCharge);//总共代付的金额
    	String totalPayStr = totalPay.multiply(new BigDecimal(100)).setScale(0).toString();//金额单位：分
    	String totalItem = records.size() + "";//单数量
    	//开始批量提现（代付）
    	List<LeScoreRecord> recordList = tranxService.batchWithdrawalLeScore(false, totalItem,  totalPayStr, records, bankCardService);
    	if (recordList != null && recordList.size() > 0) {
    		//更新已经受理的乐分提现记录（已赋值了req_sn和sn）
	    	update(recordList);
	        //批量代付（提现）的总交易批次号
	        String reqSn = recordList.get(0).getReqSn();
	        //隔一段时间请求交易结果查询接口，去更新商家货款记录的状态
	        if (reqSn != null) {
	            Timer timer=new Timer();
	            long delay = getDelayVal();//延迟10分钟后
	            long period = getPeriodVal();//每5分钟执行一次
	           
	    		  TimerTask task = new TimerTask(){
	    			public void run(){
	    				  try {
	    					    String xmlResponse = tranxService.queryTradeNew(reqSn, false);
	    					    if (xmlResponse != null) {
	    					    	//LogUtil.debug(this.getClass(), "sellerClearingCalculate", "queryTradeNew: %s", xmlResponse);
	    					        Document doc = DocumentHelper.parseText(xmlResponse);
	    					        Element root = doc.getRootElement();// AIPG
	    					        Element infoElement = root.element("INFO");
	    					        String ret_code = infoElement.elementText("RET_CODE");
	    					        String req_sn = infoElement.elementText("REQ_SN");
	    					        if ("0000".equals(ret_code)) {//处理完毕
	    					        @SuppressWarnings("unchecked")
	  								Iterator<Element> qtdetails = root.element("QTRANSRSP").elementIterator("QTDETAIL");
	    					        	while (qtdetails.hasNext()) {
	    					        		Element qtdetail = (Element) qtdetails.next();
	    					        		String sn = qtdetail.elementText("SN");
	    					        		String qtdetail_ret_code = qtdetail.elementText("RET_CODE");
	    					        		String qtdetail_err_msg = qtdetail.elementText("ERR_MSG");
	    					        		LeScoreRecord record = findNeedLeScoreRecord(reqSn, sn);
	    					        		if (record != null) {
	    			  					    	if ("0000".equals(qtdetail_ret_code) || "4000".equals(qtdetail_ret_code)) {//处理成功
	    			  					    		record.setIsWithdraw(true);
		    			  					    	record.setWithdrawMsg(qtdetail_err_msg);//提现返回消息
		    			  					    	update(record);
	    			  					    	}else {//处理失败
	    			  					    		LogUtil.debug(this.getClass(), "batchWithdrawal", qtdetail_err_msg +" for LeScoreRecord(Id): %s", record.getId());
	    			  					    		record.setIsWithdraw(false);
		    			  					    	record.setWithdrawMsg(qtdetail_err_msg);//提现返回消息
		    			  					    	update(record);
	    			  					    		// 提现失败 把 乐分还回到用户手中
	    			  					    		EndUser endUser = null;
	    			  					    		if (record.getEndUser().getId() != null) {
	    			  					    			endUser = endUserService.find(record.getEndUser().getId());
													}
	    			  					    		if (endUser != null) {
		    			  					    		// 当前乐分
		    			  					    		BigDecimal curLeScore = record.getAmount();
		    			  					    		// 激励乐分(包括乐心分红乐分，推荐获得乐分)
		    			  					    		BigDecimal motivateLeScore = record.getMotivateLeScore();
		    			  					    		// 商家直接收益乐分
		    			  					    		// BigDecimal incomeLeScore = record.getIncomeLeScore();
		    			  					    		// 代理商提成乐分
		    			  					    		BigDecimal agentLeScore = record.getAgentLeScore();
		    			  					    		if (endUser.getCurLeScore() != null) {
		    			  					    			endUser.setCurLeScore(endUser.getCurLeScore().add(curLeScore));
														}
		    			  					    		if (endUser.getMotivateLeScore() != null) {
		    			  					    			endUser.setMotivateLeScore(endUser.getMotivateLeScore().add(motivateLeScore));
														}
		    			  					    		if (endUser.getAgentLeScore() != null) {
		    			  					    			endUser.setAgentLeScore(endUser.getAgentLeScore().add(agentLeScore));
														}
		    			  					    		// endUser.setIncomeLeScore(endUser.getIncomeLeScore().add(incomeLeScore));
		    			  					    		endUserService.update(endUser);
													}

	    			  					    	}
	    			  					    	
	    					        		}
	    					        		LogUtil.debug(this.getClass(), "batchWithdrawal", "req_sn: %s, sn: %s", req_sn, sn);
	    					            }
	    					        	cancel();//结束
	    							}
	    						}
	    				} catch (Exception e) {
	    					LogUtil.debug(this.getClass(), "batchWithdrawal", "Catch Exception: %s", e.getMessage());
	    				}
	    			}
	    		};
	    		timer.schedule(task, delay, period);//延迟20分钟后，每5分钟执行一次
	  	  }
    	}else {
    		LogUtil.debug(this.getClass(), "batchWithdrawal", "Batch Withdrawal failed, recordList is null or size=0");
    		return Message.success("批量提现执行失败");
		}
	} catch (Exception e) {
		e.printStackTrace();
		LogUtil.debug(this.getClass(), "batchWithdrawal", "Batch Withdrawal failed, Catch exception: %s", e.getMessage());
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚当前的事物
	}
    
    return Message.success("批量提现执行成功");
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
  /**
   * 根据reqSn和sn，获取需要更新提现状态的乐分提现记录
   * @return
   */
  private LeScoreRecord findNeedLeScoreRecord(String reqSn, String sn){
	  LeScoreRecord record = null;
	  List<Filter> filters = new ArrayList<Filter>();
	  filters.add(Filter.eq("reqSn", reqSn));//批量代付总单号
	  filters.add(Filter.eq("sn", sn));//子单号
	  //filters.add(Filter.eq("isWithdraw", false));//未结算
	  List<LeScoreRecord> records = leScoreRecordDao.findList(null, null, filters, null);
	  if (records != null && records.size() > 0) {
		  if (records.size() > 1) {
			  //这种情况不应该出现
			  LogUtil.debug(this.getClass(), "findNeedLeScoreRecord", "Find more than one record by req_sn: %s, sn: %s", reqSn, sn);
		  }
		  record = records.get(0);
	  }
	  return record;
  }
  /**
   * 交易结果查询  延迟查询时间
   * @return
   */
  private long getDelayVal(){
	  long delay = 600000;//10分钟
	  try {
	      ParamConfig config = paramConfigService.getConfigByKey(ParamConfigKey.ALLINPAY_QUERY_DELAY);
	      if (config != null && config.getConfigValue() != null) {
	    	  delay = new Long(config.getConfigValue());
	      }
	  } catch (Exception e) {
		  LogUtil.debug(this.getClass(), "getDelayVal", "Catch Exception: %s", e.getMessage());
	  }
      return delay;
  }
  /**
   * 交易结果查询  间隔时间(每五分钟)
   * @return
   */
  private long getPeriodVal(){
	  long period = 300000;//5分钟
	  try {
	      ParamConfig periodConfig = paramConfigService.getConfigByKey(ParamConfigKey.ALLINPAY_QUERY_PERIOD);
	      if (periodConfig != null && periodConfig.getConfigValue() != null) {
	    	  period = new Long(periodConfig.getConfigValue());
	      } 
	  } catch (Exception e) {
		  LogUtil.debug(this.getClass(), "getDelayVal", "Catch Exception: %s", e.getMessage());
	  }
      return period;
  }
}

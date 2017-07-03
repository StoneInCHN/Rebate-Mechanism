package org.rebate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.dao.LeScoreRecordDao;
import org.rebate.entity.BankCard;
import org.rebate.entity.EndUser;
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;
import org.rebate.entity.commonenum.CommonEnum.ClearingStatus;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.BankCardService;
import org.rebate.service.EndUserService;
import org.rebate.service.LeScoreRecordService;
import org.rebate.service.ParamConfigService;
import org.rebate.service.SystemConfigService;
import org.rebate.utils.LogUtil;
import org.rebate.utils.SpringUtils;
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
        endUser.setCurLeScore(endUser.getCurLeScore().add(curLeScore.abs()));
        endUser.setMotivateLeScore(endUser.getMotivateLeScore().add(motivateLeScore.abs()));
    //    endUser.setIncomeLeScore(endUser.getIncomeLeScore().add(incomeLeScore));
        endUser.setAgentLeScore(endUser.getAgentLeScore().add(agentLeScore.abs()));
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
  public String batchWithdrawal(Long[] ids) {

	List<LeScoreRecord> records = new ArrayList<LeScoreRecord>();
	BigDecimal totalClearingAmount = new BigDecimal(0);//乐分提现总金额
	BigDecimal totalHandlingCharge = new BigDecimal(0);//手续费总金额
    for (Long id : ids) {
    	  LeScoreRecord record = leScoreRecordDao.find(id);
    	  if (record != null && record.getWithdrawStatus() == ApplyStatus.AUDIT_PASSED && 
    	      (record.getIsWithdraw() == null || record.getIsWithdraw() == false) ) {//未提现并且审核通过的乐分记录
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
			}else if(record.getAmount() != null) {
				totalClearingAmount = totalClearingAmount.add(record.getAmount().abs());
				BigDecimal handingCharge = getAllinpayHandlingCharge(record.getAmount().abs());
 				if (handingCharge != null) {
  					 //因为手续费要在提现金额里面扣除，所以提现金额应该至少多余手续费一分钱
  					 //否者放弃提现此单，标明备注：提现金额不够支付手续费！
  					 BigDecimal payAmount = record.getAmount().abs().subtract(handingCharge);
  					 if (payAmount.subtract(new BigDecimal(0.01)).signum() <= 0) {
  						 LogUtil.debug(this.getClass(), "batchWithdrawal", "Withdrawal Amount: %s is less than Handling Charge: %s !!!", record.getAmount().abs(), handingCharge);
  						 record.setWithdrawMsg("提现金额不够支付手续费！");
  						 update(record);
  						 continue;
  					 }
  				}
 				record.setHandlingCharge(handingCharge);//手续费
 				record.setIsWithdraw(false);//暂时先标记为已提现
 				record.setStatus(ClearingStatus.PROCESSING);
 				record.setWithdrawMsg("处理中...");//处理中...
 				totalHandlingCharge = totalHandlingCharge.add(record.getHandlingCharge()); //累加手续费
				records.add(record);
			}
		}
    }
    if (records.size() == 0) {
    	LogUtil.debug(this.getClass(), "batchWithdrawal", "没有符合批量提现的记录");
    	return null;
	}

    TranxServiceImpl tranxService = new TranxServiceImpl();
    tranxService.init();//初始化通联基础数据
    try {
    	BigDecimal totalPay = totalClearingAmount.subtract(totalHandlingCharge);//总共代付的金额
    	String totalPayStr = totalPay.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString();//金额单位：分
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
	        	return reqSn;
	        }
    	}else {
    		LogUtil.debug(this.getClass(), "batchWithdrawal", "Batch Withdrawal failed, recordList is null or size=0");
    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚当前的事物
    		return null;
		}
	} catch (Exception e) {
		e.printStackTrace();
		LogUtil.debug(this.getClass(), "batchWithdrawal", "Batch Withdrawal failed, Catch exception: %s", e.getMessage());
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚当前的事物
	}
    
    return null;
  }
	/**
	 * 单笔实时提现（代付）
	 */
	@Override
	public Message singlePay(LeScoreRecord record, BankCard bankCard) {
	      try {
			    TranxServiceImpl tranxService = new TranxServiceImpl();
			    tranxService.init();//初始化通联基础数据
			    BigDecimal handingCharge = getAllinpayHandlingCharge(record.getAmount());//手续费
			    BigDecimal payAmount = record.getAmount().abs().subtract(handingCharge);
			    //因为手续费要在结算金额里面扣除，所以结算金额应该至少多余手续费一分钱
			    //否者放弃代付此单，同时将其设置为处理失败，标明备注：结算金额不够支付手续费！方便后台手动处理
			    if (payAmount.subtract(new BigDecimal(0.01)).signum() <= 0) {
			    	record.setStatus(ClearingStatus.FAILED);
			    	record.setIsWithdraw(false);
			    	record.setRemark(SpringUtils.getMessage("rebate.sellerClearingRecord.incomeAmount.less.than.handlingCharge"));
			    	update(record);
			    	LogUtil.debug(this.getClass(), "singlePay", "Withdraw Amount: %s is less than Handling Charge: %s !!!", record.getAmount(), handingCharge);
			    	return Message.success("rebate.sellerClearingRecord.incomeAmount.less.than.handlingCharge");
			    }
					 
			    Map<String, String> resultMap =  tranxService.singleDaiFushi(false, bankCard.getOwnerName(), bankCard.getCardNum(), payAmount.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString());
			    if (resultMap.containsKey("status") && resultMap.containsKey("req_sn")){
			    	String status = resultMap.get("status");
			    	if ("success".equals(status)) {
			    		record.setStatus(ClearingStatus.SUCCESS);
				    	record.setIsWithdraw(true);
					}else if ("error".equals(status)) {
						record.setStatus(ClearingStatus.FAILED);
						record.setIsWithdraw(false);
					}
			    	record.setReqSn(resultMap.get("req_sn"));
			    	record.setSn(null);//单笔不像批量，没有sn号
			    	record.setRemark(resultMap.get("err_msg"));
			    	update(record);
				}
	        } catch (Exception e) {
	          e.printStackTrace();
	          return Message.error("rebate.common.system.error");
	        }
	        return Message.success("rebate.message.success");
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
}

package org.rebate.service.impl; 

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.dao.SystemWithdrawRecordDao;
import org.rebate.entity.Admin;
import org.rebate.entity.BankCard;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.SystemWithdrawRecord;
import org.rebate.entity.commonenum.CommonEnum.ClearingStatus;
import org.rebate.entity.commonenum.CommonEnum.PaymentChannel;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.job.SystemWithdrawSingleJob;
import org.rebate.service.SystemConfigService;
import org.rebate.service.SystemWithdrawRecordService;
import org.rebate.utils.CommonUtils;
import org.rebate.utils.LogUtil;
import org.rebate.utils.allinpay.service.TranxServiceImpl;
import org.rebate.utils.jiupai.pojo.capSingleTransfer.SingleTransferReq;
import org.rebate.utils.jiupai.service.GateWayService;
import org.springframework.stereotype.Service;

@Service("systemWithdrawRecordServiceImpl")
public class SystemWithdrawRecordServiceImpl extends BaseServiceImpl<SystemWithdrawRecord,Long> implements SystemWithdrawRecordService {

      @Resource(name="systemWithdrawRecordDaoImpl")
      private SystemWithdrawRecordDao systemWithdrawRecordDao;
      @Resource(name="systemConfigServiceImpl")
      private SystemConfigService systemConfigService;
      
      @Resource(name = "systemWithdrawSingleJob")
      private SystemWithdrawSingleJob systemWithdrawSingleJob;
      
      @Resource(name="systemWithdrawRecordDaoImpl")
      public void setBaseDao(SystemWithdrawRecordDao systemWithdrawRecordDao) {
         super.setBaseDao(systemWithdrawRecordDao);
      }

    /**
     * 平台单笔实时付款（代付）
     */
    @Override
    public Message singlePay(Admin admin, BigDecimal amount, BankCard bankCard) {
          try {
                TranxServiceImpl tranxService = new TranxServiceImpl();
                SystemWithdrawRecord record = new SystemWithdrawRecord();
                record.setOperator(admin.getUsername());
                record.setCellPhoneNum(admin.getCellPhoneNum());
                BigDecimal handlingCharge = getAllinpayHandlingCharge(amount);//手续费
                record.setHandlingCharge(handlingCharge);
                record.setAmount(amount);
                record.setBankCardId(bankCard.getId());
                record.setCardNum(bankCard.getCardNum());
                BigDecimal payAmount = amount.abs().subtract(handlingCharge);

                if (payAmount.subtract(new BigDecimal(0.01)).signum() <= 0) {
                    record.setWithdrawMsg("平台提现金额不够支付手续费");
                    record.setStatus(ClearingStatus.FAILED);
                    record.setIsWithdraw(false);
                    save(record);
                    LogUtil.debug(this.getClass(), "singlePay", "(平台提现)提现金额:%s 不够支付手续费:%s", record.getAmount(), handlingCharge);
                    return Message.error("平台提现金额不够支付手续费");
                }
                String payPenny = payAmount.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
                //获取支付渠道
                PaymentChannel channel = CommonUtils.getPaymentChannel();
                record.setPaymentChannel(channel);
                //1. 通联支付渠道
                if (PaymentChannel.ALLINPAY == channel) {
                    Map<String, String> resultMap =  tranxService.singleDaiFushi(false, bankCard.getOwnerName(), bankCard.getCardNum(), payPenny);
                    if (resultMap.containsKey("status") && resultMap.containsKey("req_sn")){
                        String status = resultMap.get("status");
                        if ("success".equals(status) || "error".equals(status)){
                            String req_sn = resultMap.get("req_sn");
                            String err_msg = resultMap.get("err_msg");
                            record.setReqSn(req_sn);
                            record.setWithdrawMsg(err_msg);
                            if ("success".equals(status)) {
                              record.setStatus(ClearingStatus.SUCCESS);
                              record.setIsWithdraw(true);
                            }else if ("error".equals(status)) {
                              record.setStatus(ClearingStatus.FAILED);
                              record.setIsWithdraw(false);
                            }else if ("wait".equals(status)){
                              //过十分钟后发起异步单笔查询请求(通联渠道)
                              systemWithdrawSingleJob.updateRecordSingleByAllinpay(req_sn);
                            }
                            save(record);//保存平台提现记录
                        }
                    }
            	}
                //2. 九派支付渠道
                else if(PaymentChannel.JIUPAI == channel) {
      	          GateWayService gateWayService = new GateWayService();
    	          SingleTransferReq req = new SingleTransferReq();
    	          req.setAmount(payPenny);//交易金额  单位：分
    	          req.setCardNo(bankCard.getCardNum());//银行卡号
    	          req.setAccName(bankCard.getOwnerName());//账户户名
    	          req.setRemark("九派 平台提现");//订单备注
    	          req.setCallBackUrl("/console/jiupai/notifySystemWithdraw.jhtml");//回调地址URL
    	          //发起单笔代付
    	          Map<String, String> resMap = gateWayService.capSingleTransfer(req, null);
    	          String rspCode = resMap.get("rspCode");//应答码  IPS00000正常返回
    	          String rspMessage = resMap.get("rspMessage");//交易成功
    	          String orderSts = resMap.get("orderSts");//订单状态  U订单初始化  P处理中   S处理成功   F处理失败   R退汇   N待人工处理
    	          String mcSequenceNo = resMap.get("mcSequenceNo");//商户交易流水
    	          String mcTransDateTime = resMap.get("mcTransDateTime");//商户交易时间
    	          String orderNo = resMap.get("orderNo");//原交易订单号
    	          if ("IPS00000".equals(rspCode) && mcSequenceNo != null) {//正常返回
                      record.setReqSn(mcSequenceNo);
                      record.setWithdrawMsg(rspMessage);
    	        	  if ("S".equals(orderSts)) {//处理成功，等待九派回调最终结果，暂时设置为处理中，未提现
    	        		  record.setStatus(ClearingStatus.PROCESSING);
                          record.setIsWithdraw(false);
                          record.setWithdrawMsg(record.getWithdrawMsg()+",等待回调通知");
    				  }
    	        	  if ("F".equals(orderSts) || "R".equals(orderSts)) {//处理失败 
    	        		  record.setStatus(ClearingStatus.FAILED);
                          record.setIsWithdraw(false);
    				  }
    	        	  if ("U".equals(orderSts) || "P".equals(orderSts) || "N".equals(orderSts)) {//处理中 
    	        		  record.setStatus(ClearingStatus.PROCESSING);
                          record.setIsWithdraw(false);
            			  //过十分钟后发起异步订单查询请求(九派渠道)
            			  systemWithdrawSingleJob.updateRecordSingleByJiuPai(mcSequenceNo, mcTransDateTime, orderNo);
    				  }
    	        	  save(record);//保存平台提现记录
    			  }
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
package org.rebate.service.impl; 

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.dao.SystemWithdrawRecordDao;
import org.rebate.entity.BankCard;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.SystemWithdrawRecord;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.SystemConfigService;
import org.rebate.service.SystemWithdrawRecordService;
import org.rebate.utils.LogUtil;
import org.rebate.utils.SpringUtils;
import org.rebate.utils.allinpay.service.TranxServiceImpl;
import org.springframework.stereotype.Service;

@Service("systemWithdrawRecordServiceImpl")
public class SystemWithdrawRecordServiceImpl extends BaseServiceImpl<SystemWithdrawRecord,Long> implements SystemWithdrawRecordService {

      @Resource(name="systemWithdrawRecordDaoImpl")
      private SystemWithdrawRecordDao systemWithdrawRecordDao;
      @Resource(name="systemConfigServiceImpl")
      private SystemConfigService systemConfigService;
      
      @Resource(name="systemWithdrawRecordDaoImpl")
      public void setBaseDao(SystemWithdrawRecordDao systemWithdrawRecordDao) {
         super.setBaseDao(systemWithdrawRecordDao);
      }

    /**
     * 平台单笔实时付款（代付）
     */
    @Override
    public Message singlePay(BigDecimal amount, BankCard bankCard) {
          try {
                TranxServiceImpl tranxService = new TranxServiceImpl();
                tranxService.init();//初始化通联基础数据
                SystemWithdrawRecord record = new SystemWithdrawRecord();
                BigDecimal handlingCharge = getAllinpayHandlingCharge(amount);//手续费
                record.setHandlingCharge(handlingCharge);
                BigDecimal payAmount = amount.abs().subtract(handlingCharge);
                //因为手续费要在结算金额里面扣除，所以结算金额应该至少多余手续费一分钱
                //否者放弃代付此单，同时将其设置为处理失败，标明备注：结算金额不够支付手续费！方便后台手动处理
                if (payAmount.subtract(new BigDecimal(0.01)).signum() <= 0) {
                    record.setRemark(SpringUtils.getMessage("rebate.sellerClearingRecord.incomeAmount.less.than.handlingCharge"));
                    update(record);
                    LogUtil.debug(this.getClass(), "singlePay", "Income Amount: %s is less than Handling Charge: %s !!!", record.getAmount(), handlingCharge);
                    return Message.success("rebate.sellerClearingRecord.incomeAmount.less.than.handlingCharge");
                }
                Map<String, String> resultMap =  tranxService.singleDaiFushi(false, bankCard.getBankName(), bankCard.getCardNum(), payAmount.multiply(new BigDecimal(100)).setScale(0).toString());
                if (resultMap.containsKey("status") && resultMap.containsKey("req_sn")){
                    String status = resultMap.get("status");
                    if ("success".equals(status) || "success".equals(status)){
//                        String req_sn = resultMap.get("req_sn");
//                        String err_msg = resultMap.get("err_msg");
//                        singlePayHandle(record, status, req_sn, err_msg, handlingCharge);
//                        record.setAmount(oldRecord.getAmount());
//                        record.setBankCardId(oldRecord.getBankCardId());
//                        String clearingSn = snService.generate(Type.SELLER_CLEARING_RECORD);
//                        record.setTotalOrderAmount(oldRecord.getTotalOrderAmount());
//                        record.setHandlingCharge(handlingCharge);
//                        record.setValid(true);//标记新的货款记录有效
//                        record.setReqSn(req_sn);
//                        record.setSn(null);//单笔结算不像批量结算，没有sn号
//                        record.setRemark(err_msg);
//                        if ("success".equals(status)) {
//                          record.setStatus(ClearingStatus.SUCCESS);
//                          record.setIsClearing(true);
//                        }else if ("error".equals(status)) {
//                          record.setStatus(ClearingStatus.FAILED);
//                          record.setIsClearing(false);
//                        }
//                        save(record);//保存新的货款记录
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
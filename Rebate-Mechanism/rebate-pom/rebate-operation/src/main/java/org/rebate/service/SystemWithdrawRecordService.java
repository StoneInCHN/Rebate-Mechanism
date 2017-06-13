package org.rebate.service; 

import java.math.BigDecimal;

import org.rebate.beans.Message;
import org.rebate.entity.Admin;
import org.rebate.entity.BankCard;
import org.rebate.entity.SystemWithdrawRecord;
import org.rebate.framework.service.BaseService;

public interface SystemWithdrawRecordService extends BaseService<SystemWithdrawRecord,Long>{

  Message singlePay(Admin admin, BigDecimal amount, BankCard bankCard);

}
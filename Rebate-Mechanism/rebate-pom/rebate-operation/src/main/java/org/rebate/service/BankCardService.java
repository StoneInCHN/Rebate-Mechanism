package org.rebate.service; 

import org.rebate.entity.BankCard;
import org.rebate.framework.service.BaseService;

public interface BankCardService extends BaseService<BankCard,Long>{
	BankCard getDefaultCard(Long userId);
}
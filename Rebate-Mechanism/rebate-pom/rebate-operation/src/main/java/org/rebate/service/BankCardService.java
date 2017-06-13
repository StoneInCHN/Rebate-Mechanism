package org.rebate.service; 

import java.util.List;

import org.rebate.entity.Admin;
import org.rebate.entity.BankCard;
import org.rebate.entity.EndUser;
import org.rebate.framework.service.BaseService;

public interface BankCardService extends BaseService<BankCard,Long>{
	/**
	 * 获取用户默认银行卡
	 * @param userId
	 * @return
	 */
	BankCard getDefaultCard(Long userId);
	/**
	 * 获取用户默认银行卡
	 * @param userId
	 * @return
	 */
	BankCard getDefaultCard(EndUser endUser);
	
	BankCard getDefaultCard(Admin admin);
	
	List<BankCard> getAllCardList(Admin admin);
}
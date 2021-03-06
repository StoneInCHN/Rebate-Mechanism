package org.rebate.service;

import org.rebate.entity.BankCard;
import org.rebate.framework.service.BaseService;
import org.rebate.json.request.BankCardRequest;

public interface BankCardService extends BaseService<BankCard, Long> {


  /**
   * 根据ID删除银行卡
   * 
   * @param cardId
   */
  void delBankCardById(Long cardId);


  /**
   * 用户添加 银行卡
   * 
   * @param cardId
   */
  BankCard addCard(BankCardRequest req);


  /**
   * 用户获取默认 银行卡
   * 
   * @param cardId
   */
  BankCard getDefaultCard(Long userId);

  /**
   * 更新为默认银行卡
   * 
   * @param bankCard
   */
  void updateCardDefault(BankCard bankCard, Long userId);

  /**
   * 判断用户是否拥有可用银行卡
   * 
   * @param userId
   * @return
   */
  Boolean userHasBankCard(Long userId);



}

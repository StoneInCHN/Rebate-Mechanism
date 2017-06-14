package org.rebate.dao;

import org.rebate.entity.BankCard;
import org.rebate.framework.dao.BaseDao;

public interface BankCardDao extends BaseDao<BankCard, Long> {
  /**
   * 银行卡四元素校验失败记录是否重复校验
   * 
   * @param ownerName
   * @param cardNum
   * @param idcard
   * @param reservedMobile
   * @return
   */
  String isVerifyFailedRecord(String ownerName, String cardNum, String idcard, String reservedMobile);

  /**
   * 银行卡四元素校验失败记录存入缓存
   * 
   * @param ownerName
   * @param cardNum
   * @param idcard
   * @param reservedMobile
   * @return
   */
  String genVerifyFailedRecord(String ownerName, String cardNum, String idcard,
      String reservedMobile, String result);
}

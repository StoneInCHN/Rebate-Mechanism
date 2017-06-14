package org.rebate.dao.impl;

import org.rebate.dao.BankCardDao;
import org.rebate.entity.BankCard;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository("bankCardDaoImpl")
public class BankCardDaoImpl extends BaseDaoImpl<BankCard, Long> implements BankCardDao {
  @Override
  @Cacheable(value = "bankCard",
      key = "'bankCard.verify='+#ownerName+#cardNum+#idcard+#reservedMobile")
  public String isVerifyFailedRecord(String ownerName, String cardNum, String idcard,
      String reservedMobile) {
    return null;
  }

  @Override
  @CachePut(value = "bankCard",
      key = "'bankCard.verify='+#ownerName+#cardNum+#idcard+#reservedMobile")
  public String genVerifyFailedRecord(String ownerName, String cardNum, String idcard,
      String reservedMobile, String result) {
    return result;
  }
}

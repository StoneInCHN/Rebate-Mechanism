package org.rebate.service.impl; 

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 

import org.rebate.entity.BankCard;
import org.rebate.dao.BankCardDao;
import org.rebate.service.BankCardService;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("bankCardServiceImpl")
public class BankCardServiceImpl extends BaseServiceImpl<BankCard,Long> implements BankCardService {

      @Resource(name="bankCardDaoImpl")
      public void setBaseDao(BankCardDao bankCardDao) {
         super.setBaseDao(bankCardDao);
  }
}
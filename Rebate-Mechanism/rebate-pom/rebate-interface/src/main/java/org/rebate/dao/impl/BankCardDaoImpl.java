package org.rebate.dao.impl; 

import org.springframework.stereotype.Repository; 

import org.rebate.entity.BankCard;
import org.rebate.framework.dao.impl.BaseDaoImpl;
import org.rebate.dao.BankCardDao;
@Repository("bankCardDaoImpl")
public class BankCardDaoImpl extends  BaseDaoImpl<BankCard,Long> implements BankCardDao {

}
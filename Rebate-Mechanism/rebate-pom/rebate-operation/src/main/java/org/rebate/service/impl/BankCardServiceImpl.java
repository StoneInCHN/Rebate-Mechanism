package org.rebate.service.impl; 

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource; 

import org.springframework.stereotype.Service; 
import org.springframework.util.CollectionUtils;
import org.rebate.entity.BankCard;
import org.rebate.entity.EndUser;
import org.rebate.dao.BankCardDao;
import org.rebate.service.BankCardService;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.service.impl.BaseServiceImpl;

@Service("bankCardServiceImpl")
public class BankCardServiceImpl extends BaseServiceImpl<BankCard,Long> implements BankCardService {

    @Resource(name="bankCardDaoImpl")
    public void setBaseDao(BankCardDao bankCardDao) {
       super.setBaseDao(bankCardDao);
    }
    @Resource(name="bankCardDaoImpl")
    private BankCardDao bankCardDao;
    
    @Override
    public BankCard getDefaultCard(Long userId) {
    	List<Filter> filters = new ArrayList<Filter>();
    	filters.add(Filter.eq("isDefault", true));
    	filters.add(Filter.eq("endUser", userId));
    	List<BankCard> cards = bankCardDao.findList(0, 1, filters, null);
    	if (!CollectionUtils.isEmpty(cards)) {
    		return cards.get(0);
    	}
    	return null;
    }
    @Override
	public BankCard getDefaultCard(EndUser endUser) {
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.eq("isDefault", true));
        filters.add(Filter.eq("endUser", endUser));
        List<BankCard> cards = bankCardDao.findList(0, 1, filters, null);
        if (!CollectionUtils.isEmpty(cards)) {
          return cards.get(0);
        }
        return null;
	}
}
    
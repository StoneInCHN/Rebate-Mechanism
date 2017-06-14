package org.rebate.service.impl; 

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.dao.BankCardDao;
import org.rebate.entity.Admin;
import org.rebate.entity.BankCard;
import org.rebate.entity.EndUser;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.BankCardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
	@Override
	public BankCard getDefaultCard(Admin admin) {
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.eq("isDefault", true));
        filters.add(Filter.eq("admin", admin));
        List<BankCard> cards = bankCardDao.findList(0, 1, filters, null);
        if (!CollectionUtils.isEmpty(cards)) {
          return cards.get(0);
        }
        return null;
	}
	@Override
	public List<BankCard> getAllCardList(Admin admin) {
		List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.eq("admin", admin));
        List<BankCard> cards = bankCardDao.findList(null, null, filters, null);
        if (!CollectionUtils.isEmpty(cards)) {
          return cards;
        }
		return null;
	}
	
	  @Override
	  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	  public void updateCardDefault(BankCard bankCard, Admin admin) {
	    bankCard.setIsDefault(true);
	    List<BankCard> mergeCards = new ArrayList<BankCard>();
	    mergeCards.add(bankCard);
	    List<Filter> filters = new ArrayList<Filter>();
	    filters.add(Filter.eq("admin", admin));
	    filters.add(Filter.eq("isDefault", true));
	    List<BankCard> cards = bankCardDao.findList(null, null, filters, null);
	    for (BankCard card : cards) {
	      card.setIsDefault(false);
	      mergeCards.add(card);
	    }
	    bankCardDao.merge(mergeCards);
	  }
	  @Override
	  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	  public BankCard saveBankCard(BankCard bankCard, Admin admin) {
	    List<BankCard> mergeCards = new ArrayList<BankCard>();
	    bankCard.setAdmin(admin);
	    mergeCards.add(bankCard);
	    if (bankCard.getIsDefault() != null && bankCard.getIsDefault()) {
	      List<Filter> filters = new ArrayList<Filter>();
	      filters.add(Filter.eq("admin", admin));
	      filters.add(Filter.eq("isDefault", true));
	      List<BankCard> cards = bankCardDao.findList(null, null, filters, null);
	      for (BankCard card : cards) {
	        if (card.getIsDefault()) {
	          card.setIsDefault(false);
	          mergeCards.add(card);
	        }
	      }
	    }else {
	      bankCard.setIsDefault(false);
        }
	    bankCardDao.merge(mergeCards);
	    return bankCard;
	  }
    @Override
    public long countAllCardList(Admin admin) {
      long count = bankCardDao.count(Filter.eq("admin", admin));
      return count;
    }
}
    
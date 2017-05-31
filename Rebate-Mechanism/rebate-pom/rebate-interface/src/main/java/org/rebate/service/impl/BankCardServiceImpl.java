package org.rebate.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.dao.BankCardDao;
import org.rebate.dao.EndUserDao;
import org.rebate.dao.UserAuthDao;
import org.rebate.entity.BankCard;
import org.rebate.entity.EndUser;
import org.rebate.entity.UserAuth;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.ordering.Ordering;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.json.request.BankCardRequest;
import org.rebate.service.BankCardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service("bankCardServiceImpl")
public class BankCardServiceImpl extends BaseServiceImpl<BankCard, Long> implements BankCardService {

  @Resource(name = "bankCardDaoImpl")
  private BankCardDao bankCardDao;

  @Resource(name = "endUserDaoImpl")
  private EndUserDao endUserDao;
  @Resource(name = "userAuthDaoImpl")
  private UserAuthDao userAuthDao;

  @Resource(name = "bankCardDaoImpl")
  public void setBaseDao(BankCardDao bankCardDao) {
    super.setBaseDao(bankCardDao);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void delBankCardById(Long cardId) {
    BankCard bankCard = bankCardDao.find(cardId);
    if (!bankCard.getIsDefault()) {
      bankCard.setDelStatus(true);
      bankCardDao.merge(bankCard);
    } else {
      EndUser endUser = bankCard.getEndUser();
      bankCard.setDelStatus(true);
      bankCardDao.merge(bankCard);

      List<Filter> filters = new ArrayList<Filter>();
      filters.add(Filter.eq("isDefault", false));
      filters.add(Filter.eq("endUser", endUser));
      filters.add(Filter.eq("delStatus", false));

      List<Ordering> orderings = new ArrayList<Ordering>();
      orderings.add(Ordering.desc("createDate"));

      List<BankCard> cards = bankCardDao.findList(0, 1, filters, orderings);
      if (!CollectionUtils.isEmpty(cards)) {
        BankCard card = cards.get(0);
        card.setIsDefault(true);
        bankCardDao.merge(card);
      }
    }

  }

  @Override
  public BankCard addCard(BankCardRequest req) {
    EndUser endUser = endUserDao.find(req.getUserId());
    List<BankCard> mergeCards = new ArrayList<BankCard>();
//    //是否是已逻辑删的银行卡，如果是，则恢复，否者新添加
//	List<Filter> filterList = new ArrayList<Filter>();
//	filterList.add(Filter.eq("cardNum", req.getCardNum()));
//	filterList.add(Filter.eq("ownerName", req.getOwnerName()));
//	filterList.add(Filter.eq("idCard", req.getIdCard()));
//	filterList.add(Filter.eq("reservedMobile", req.getReservedMobile()));
//	filterList.add(Filter.eq("delStatus", true));
//	List<BankCard> bankCards = findList(1, filterList, null);
//    if (bankCards != null && bankCards.size() > 0) {
//    	BankCard bankCard = bankCards.get(0);
//    	bankCard.setDelStatus(false);
//    	bankCard.setEndUser(endUser);
//    	mergeCards.add(bankCard);
//	}else {
	    BankCard bankCard = new BankCard();
	    bankCard.setEndUser(endUser);
	    bankCard.setOwnerName(req.getOwnerName());
	    bankCard.setBankName(req.getBankName());
	    bankCard.setCardNum(req.getCardNum());
	    bankCard.setIdCard(req.getIdCard());
	    bankCard.setCardType(req.getCardType());
	    bankCard.setIsDefault(req.getIsDefault());
	    bankCard.setReservedMobile(req.getReservedMobile());
	    bankCard.setBankLogo(req.getBankLogo());
	    bankCard.setDelStatus(false);
	    mergeCards.add(bankCard);
//	}

    if (req.getIsDefault() != null && req.getIsDefault()) {
      List<Filter> filters = new ArrayList<Filter>();
      filters.add(Filter.eq("endUser", endUser));
      filters.add(Filter.eq("isDefault", true));
      List<BankCard> cards = bankCardDao.findList(null, null, filters, null);
      for (BankCard card : cards) {
        if (card.getIsDefault()) {
          card.setIsDefault(false);
          mergeCards.add(card);
        }
      }
    }

    bankCardDao.merge(mergeCards);
    UserAuth userAuth = userAuthDao.getUserAuth(req.getUserId(), false);
    if (userAuth != null) {
      userAuth.setIsAuth(true);
      userAuthDao.merge(userAuth);
    }
    return bankCard;
  }

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
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void updateCardDefault(BankCard bankCard, Long userId) {
    bankCard.setIsDefault(true);
    List<BankCard> mergeCards = new ArrayList<BankCard>();
    mergeCards.add(bankCard);
    List<Filter> filters = new ArrayList<Filter>();
    filters.add(Filter.eq("endUser", userId));
    filters.add(Filter.eq("isDefault", true));
    List<BankCard> cards = bankCardDao.findList(null, null, filters, null);
    for (BankCard card : cards) {
      card.setIsDefault(false);
      mergeCards.add(card);
    }
    bankCardDao.merge(mergeCards);
  }
}

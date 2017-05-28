package org.rebate.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.dao.BankCardDao;
import org.rebate.dao.EndUserDao;
import org.rebate.entity.BankCard;
import org.rebate.entity.EndUser;
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

  @Resource(name = "bankCardDaoImpl")
  public void setBaseDao(BankCardDao bankCardDao) {
    super.setBaseDao(bankCardDao);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void delBankCardById(Long cardId) {
    BankCard bankCard = bankCardDao.find(cardId);
    if (!bankCard.getIsDefault()) {
      bankCardDao.remove(bankCard);
    } else {
      EndUser endUser = bankCard.getEndUser();
      bankCardDao.remove(bankCard);

      List<Filter> filters = new ArrayList<Filter>();
      filters.add(Filter.eq("isDefault", false));
      filters.add(Filter.eq("endUser", endUser));

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

    List<BankCard> mergeCards = new ArrayList<BankCard>();
    mergeCards.add(bankCard);

    if (req.getIsDefault()) {
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
}

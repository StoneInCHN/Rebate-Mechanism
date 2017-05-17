package org.rebate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.dao.SellerDao;
import org.rebate.dao.SellerEvaluateDao;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerEvaluate;
import org.rebate.entity.commonenum.CommonEnum.CommonStatus;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.SellerEvaluateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("sellerEvaluateServiceImpl")
public class SellerEvaluateServiceImpl extends BaseServiceImpl<SellerEvaluate, Long> implements
    SellerEvaluateService {

  @Resource(name = "sellerEvaluateDaoImpl")
  private SellerEvaluateDao sellerEvaluateDao;

  @Resource(name = "sellerDaoImpl")
  private SellerDao sellerDao;

  @Resource(name = "sellerEvaluateDaoImpl")
  public void setBaseDao(SellerEvaluateDao sellerEvaluateDao) {
    super.setBaseDao(sellerEvaluateDao);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Boolean changeEvaluateStatus(Long[] Ids, CommonStatus oprStatus) {
    List<SellerEvaluate> evaluates = new ArrayList<SellerEvaluate>();
    for (Long id : Ids) {
      SellerEvaluate evaluate = sellerEvaluateDao.find(id);
      if (CommonStatus.ACITVE.equals(oprStatus)
          && CommonStatus.INACTIVE.equals(evaluate.getStatus())) {// 启用操作，过滤禁用状态的评价
        evaluates.add(evaluate);
      }
      if (CommonStatus.INACTIVE.equals(oprStatus)
          && CommonStatus.ACITVE.equals(evaluate.getStatus())) {// 禁用操作，过滤启用状态的评价
        evaluates.add(evaluate);
      }
    }

    for (SellerEvaluate evaluate : evaluates) {
      Seller seller = evaluate.getSeller();
      BigDecimal rateScore = seller.getRateScore();
      Integer rateCounts = seller.getRateCounts();
      if (CommonStatus.ACITVE.equals(oprStatus)) {// 启用操作
        evaluate.setStatus(CommonStatus.ACITVE);
        rateCounts = seller.getRateCounts() + 1;
        BigDecimal totalScore =
            rateScore.multiply(new BigDecimal(seller.getRateCounts())).add(
                new BigDecimal(evaluate.getScore()));
        rateScore = totalScore.divide(new BigDecimal(rateCounts), 1, BigDecimal.ROUND_HALF_UP);

      }
      if (CommonStatus.INACTIVE.equals(oprStatus)) {// 禁用操作
        evaluate.setStatus(CommonStatus.INACTIVE);
        rateCounts = seller.getRateCounts() - 1;
        if (rateCounts > 0) {
          BigDecimal totalScore =
              rateScore.multiply(new BigDecimal(seller.getRateCounts())).subtract(
                  new BigDecimal(evaluate.getScore()));

          rateScore = totalScore.divide(new BigDecimal(rateCounts), 1, BigDecimal.ROUND_HALF_UP);
        } else {
          rateScore = new BigDecimal("5");
        }
      }
      seller.setRateCounts(rateCounts);
      seller.setRateScore(rateScore);
      sellerEvaluateDao.merge(evaluate);
      sellerDao.merge(seller);
    }

    return true;
  }
}

package org.rebate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.dao.EndUserDao;
import org.rebate.dao.LeMindRecordDao;
import org.rebate.dao.OrderDao;
import org.rebate.dao.SystemConfigDao;
import org.rebate.entity.EndUser;
import org.rebate.entity.Order;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.EndUserService;
import org.rebate.utils.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service("endUserServiceImpl")
public class EndUserServiceImpl extends BaseServiceImpl<EndUser, Long> implements EndUserService {

  @Resource(name = "endUserDaoImpl")
  private EndUserDao endUserDao;

  @Resource(name = "leMindRecordDaoImpl")
  private LeMindRecordDao leMindRecordDao;

  @Resource(name = "systemConfigDaoImpl")
  private SystemConfigDao systemConfigDao;

  @Resource(name = "orderDaoImpl")
  private OrderDao orderDao;


  @Resource(name = "endUserDaoImpl")
  public void setBaseDao(EndUserDao endUserDao) {
    super.setBaseDao(endUserDao);
  }


  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void dailyBonusCalJob() {

    Calendar startTime = Calendar.getInstance();
    startTime.setTime(new Date());
    startTime.add(Calendar.DATE, -1);
    startTime.set(Calendar.HOUR_OF_DAY, 0);
    startTime.set(Calendar.MINUTE, 0);
    startTime.set(Calendar.SECOND, 0);
    startTime.set(Calendar.MILLISECOND, 0);

    Calendar endTime = Calendar.getInstance();
    endTime.setTime(new Date());
    endTime.add(Calendar.DATE, -1);
    endTime.set(Calendar.HOUR_OF_DAY, 23);
    endTime.set(Calendar.MINUTE, 59);
    endTime.set(Calendar.SECOND, 59);
    endTime.set(Calendar.MILLISECOND, 999);

    SystemConfig totalBonusPerConfig =
        systemConfigDao.getConfigByKey(SystemConfigKey.TOTAL_BONUS_PERCENTAGE);
    if (totalBonusPerConfig == null || totalBonusPerConfig.getConfigValue() == null) {
      if (LogUtil.isDebugEnabled(EndUserServiceImpl.class)) {
        LogUtil.debug(EndUserServiceImpl.class, "dailyBonusCalJob",
            "daily Bonus calculate Job. Timer Period: %s, total bonus percentage config no exist!",
            startTime + "-" + endTime);
      }
      return;
    }

    List<Filter> filters = new ArrayList<Filter>();
    Filter start = new Filter("createDate", Operator.ge, startTime);
    Filter end = new Filter("createDate", Operator.le, endTime);
    filters.add(start);
    filters.add(end);
    List<Order> orders = orderDao.findList(null, null, filters, null);
    if (CollectionUtils.isEmpty(orders)) {
      if (LogUtil.isDebugEnabled(EndUserServiceImpl.class)) {
        LogUtil.debug(EndUserServiceImpl.class, "dailyBonusCalJob",
            "daily Bonus calculate Job. Timer Period: %s, search orders no exist!", startTime + "-"
                + endTime);
      }
      return;
    }

    BigDecimal totalBonus = new BigDecimal("0");
    for (Order order : orders) {
      totalBonus = totalBonus.add(order.getRebateAmount());
    }
    if (totalBonus.compareTo(new BigDecimal("0")) <= 0) {
      if (LogUtil.isDebugEnabled(EndUserServiceImpl.class)) {
        LogUtil.debug(EndUserServiceImpl.class, "dailyBonusCalJob",
            "daily Bonus calculate Job. Timer Period: %s, search orders no exist", startTime + "-"
                + endTime);
      }
      return;
    }
    /**
     * 每日分红的金额
     */
    totalBonus = totalBonus.multiply(new BigDecimal(totalBonusPerConfig.getConfigValue()));
  }

  @Override
  public EndUser findByUserName(String userName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EndUser findByUserMobile(String mobileNo) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getEndUserToken(Long id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String createEndUserToken(String token, Long id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public AppPlatform getEndUserAppPlatform(Long id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public AppPlatform createEndUserAppPlatform(AppPlatform appPlatform, Long id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteEndUserToken(Long id) {
    // TODO Auto-generated method stub

  }

  @Override
  public EndUser userReg(String userName, String password) {
    // TODO Auto-generated method stub
    return null;
  }



}

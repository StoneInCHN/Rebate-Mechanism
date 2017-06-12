package org.rebate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.beans.Setting;
import org.rebate.dao.BonusParamPerDayDao;
import org.rebate.dao.EndUserDao;
import org.rebate.dao.LeMindRecordDao;
import org.rebate.dao.OrderDao;
import org.rebate.dao.SystemConfigDao;
import org.rebate.entity.Area;
import org.rebate.entity.BonusByMindPerDay;
import org.rebate.entity.BonusParamPerDay;
import org.rebate.entity.EndUser;
import org.rebate.entity.LeBeanRecord;
import org.rebate.entity.LeMindRecord;
import org.rebate.entity.LeScoreRecord;
import org.rebate.entity.Order;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.rebate.entity.commonenum.CommonEnum.CommonStatus;
import org.rebate.entity.commonenum.CommonEnum.LeBeanChangeType;
import org.rebate.entity.commonenum.CommonEnum.LeScoreType;
import org.rebate.entity.commonenum.CommonEnum.OrderStatus;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.filter.Filter;
import org.rebate.framework.filter.Filter.Operator;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.EndUserService;
import org.rebate.service.HolidayConfigService;
import org.rebate.service.MailService;
import org.rebate.utils.LogUtil;
import org.rebate.utils.SettingUtils;
import org.rebate.utils.TimeUtils;
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

  @Resource(name = "bonusParamPerDayDaoImpl")
  private BonusParamPerDayDao bonusParamPerDayDao;

  @Resource(name = "holidayConfigServiceImpl")
  private HolidayConfigService holidayConfigService;

  @Resource(name = "mailServiceImpl")
  private MailService mailService;


  @Resource(name = "endUserDaoImpl")
  public void setBaseDao(EndUserDao endUserDao) {
    super.setBaseDao(endUserDao);
  }


  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void dailyBonusCalJob(Date startTime, Date endTime) {
    Setting setting = SettingUtils.get();
    String subject =
        "yxsh:daily bonus calculate job notice email(server ip:" + setting.getServerIp() + ")";
    String emailTo = "sujinxuan123@163.com,sj_msc@163.com";
    // String emailTo = "sujinxuan123@163.com";
    String msg = "";
    BonusParamPerDay bonusParamPerDay = new BonusParamPerDay();
    bonusParamPerDay.setBonusDate(startTime);
    /**
     * 乐心分红value值
     */
    SystemConfig mindValueConfig = systemConfigDao.getConfigByKey(SystemConfigKey.MIND_VALUE);
    BigDecimal value = new BigDecimal("0");
    if (mindValueConfig != null && mindValueConfig.getConfigValue() != null) {
      // 系统配置的value值
      value = new BigDecimal(mindValueConfig.getConfigValue());
      bonusParamPerDay.setCalValue(value.toString());
    }

    try {
      Boolean isHoliday = holidayConfigService.isHoliday(startTime);
      if (isHoliday) {
        if (LogUtil.isDebugEnabled(EndUserServiceImpl.class)) {
          LogUtil
              .debug(
                  EndUserServiceImpl.class,
                  "dailyBonusCalJob",
                  "daily Bonus calculate 每日分红运算失败! Timer Period: %s, The date for calculating is holiday!",
                  startTime + "-" + endTime);
        }
        msg = "每日分红运算失败!\n" + TimeUtils.format("yyyy-MM-dd", startTime.getTime()) + "为法定节假日,不计算分红!";
        bonusParamPerDay.setRemark(msg);
        return;
      }
      /**
       * 每日分红总金额占平台每日总收益的比例
       */
      SystemConfig totalBonusPerConfig =
          systemConfigDao.getConfigByKey(SystemConfigKey.TOTAL_BONUS_PERCENTAGE);
      if (totalBonusPerConfig == null || totalBonusPerConfig.getConfigValue() == null) {
        if (LogUtil.isDebugEnabled(EndUserServiceImpl.class)) {
          LogUtil
              .debug(
                  EndUserServiceImpl.class,
                  "dailyBonusCalJob",
                  "daily Bonus calculate 每日分红运算失败! Timer Period: %s, total bonus percentage config no exist!",
                  startTime + "-" + endTime);
        }
        msg = "每日分红运算失败!\n参数未配置：每日分红总金额占平台每日总收益的比例";
        bonusParamPerDay.setRemark(msg);
        return;
      }
      bonusParamPerDay.setTotalBonusPerConfig(totalBonusPerConfig.getConfigValue());

      /**
       * 收益后乐分乐豆比例
       */
      SystemConfig leScorePerConfig =
          systemConfigDao.getConfigByKey(SystemConfigKey.LESCORE_PERCENTAGE);
      if (leScorePerConfig == null || leScorePerConfig.getConfigValue() == null) {
        if (LogUtil.isDebugEnabled(EndUserServiceImpl.class)) {
          LogUtil
              .debug(
                  EndUserServiceImpl.class,
                  "dailyBonusCalJob",
                  "daily Bonus calculate 每日分红运算失败! Timer Period: %s, bonus leScore percentage config no exist!",
                  startTime + "-" + endTime);
        }
        msg = "每日分红运算失败!\n参数未配置：激励收益后乐分乐豆比例";
        bonusParamPerDay.setRemark(msg);
        return;
      }
      bonusParamPerDay.setLeScorePerConfig(leScorePerConfig.getConfigValue());

      List<Filter> filters = new ArrayList<Filter>();
      Filter start = new Filter("paymentTime", Operator.ge, startTime);
      Filter end = new Filter("paymentTime", Operator.le, endTime);
      Filter status = new Filter("status", Operator.ne, OrderStatus.UNPAID);
      filters.add(status);
      filters.add(start);
      filters.add(end);
      List<Order> orders = orderDao.findList(null, null, filters, null);
      if (CollectionUtils.isEmpty(orders)) {
        if (LogUtil.isDebugEnabled(EndUserServiceImpl.class)) {
          LogUtil.debug(EndUserServiceImpl.class, "dailyBonusCalJob",
              "daily Bonus calculate 每日分红运算失败! Timer Period: %s, search orders no exist!",
              startTime + "-" + endTime);
        }
        msg = "每日分红运算失败!\n当日平台未产生有效的支付订单，无法计算分红";
        bonusParamPerDay.setOrderCount(0);
        bonusParamPerDay.setRemark(msg);
        return;
      }
      bonusParamPerDay.setOrderCount(orders.size());

      BigDecimal totalBonus = new BigDecimal("0");
      for (Order order : orders) {
        totalBonus = totalBonus.add(order.getRebateAmount());
      }
      if (totalBonus.compareTo(new BigDecimal("0")) <= 0) {
        if (LogUtil.isDebugEnabled(EndUserServiceImpl.class)) {
          LogUtil.debug(EndUserServiceImpl.class, "dailyBonusCalJob",
              "daily Bonus calculate 每日分红运算失败! Timer Period: %s, the platform total income<=0",
              startTime + "-" + endTime);
        }
        msg = "每日分红运算失败!\n当日平台总收益金额为" + totalBonus.toString() + "元,无法计算分红";
        bonusParamPerDay.setRebateTotalAmount(totalBonus.toString());
        bonusParamPerDay.setRemark(msg);
        return;
      }
      bonusParamPerDay.setRebateTotalAmount(totalBonus.toString());

      /**
       * 每日用于分红计算的总金额
       */
      BigDecimal totalBonusPer =
          totalBonus.multiply(new BigDecimal(totalBonusPerConfig.getConfigValue())).setScale(4,
              BigDecimal.ROUND_HALF_UP);

      bonusParamPerDay.setBonusCalAmount(totalBonus.toString());

      // /**
      // * 计算每日乐心大于等于1的用户
      // */
      // List<EndUser> endUsers = endUserDao.getMindUsersByDay(startTime, endTime);
      // if (CollectionUtils.isEmpty(endUsers)) {
      // if (LogUtil.isDebugEnabled(EndUserServiceImpl.class)) {
      // LogUtil.debug(EndUserServiceImpl.class, "dailyBonusCalJob",
      // "daily Bonus calculate 每日分红运算失败! Timer Period: %s, no users exchange leMind",
      // startTime + "-" + endTime);
      // }
      // msg = "每日分红运算失败!\n当日平台消费产生乐心大于等于1的用户数量为0,无法计算分红";
      // bonusParamPerDay.setRemark(msg);
      // bonusParamPerDay.setLeMindUserCount(0);
      // return;
      // }
      // bonusParamPerDay.setLeMindUserCount(endUsers.size());
      //
      // /**
      // * 乐心的每天的市值计算(取0.8~1.2的随机数产生，保留两位小数) [0.8,1.2)
      // */
      // BigDecimal value =
      // new BigDecimal(Math.random() * 4 + 8).divide(new BigDecimal("10"), 2,
      // BigDecimal.ROUND_HALF_UP);
      // // BigDecimal value =
      // // totalBonus.divide(new BigDecimal(endUsers.size()), 2, BigDecimal.ROUND_HALF_UP);



      List<Filter> mindFilters = new ArrayList<Filter>();
      Filter statusFilter = new Filter("status", Operator.eq, CommonStatus.ACITVE);
      mindFilters.add(statusFilter);
      List<LeMindRecord> leMindRecords = leMindRecordDao.findList(null, null, mindFilters, null);
      if (CollectionUtils.isEmpty(leMindRecords)) {
        if (LogUtil.isDebugEnabled(EndUserServiceImpl.class)) {
          LogUtil.debug(EndUserServiceImpl.class, "dailyBonusCalJob",
              "daily Bonus calculate 每日分红运算失败! Timer Period: %s, no active leMind records",
              startTime + "-" + endTime);
        }
        msg = "每日分红运算失败!\n当日平台无可产生用户分红的乐心,无法计算分红";
        bonusParamPerDay.setRemark(msg);
        bonusParamPerDay.setAvlLeMindCount(0);
        return;
      }

      BigDecimal leMindSize = new BigDecimal("0");
      for (LeMindRecord leMindRecord : leMindRecords) {
        leMindSize = leMindSize.add(leMindRecord.getAmount());
      }

      /**
       * 计算乐心分红value值
       */
      if (mindValueConfig == null) {
        // 计算的value值
        value = totalBonusPer.divide(leMindSize, 4, BigDecimal.ROUND_HALF_UP);
        bonusParamPerDay.setCalValue(value.toString());
      }


      bonusParamPerDay.setAvlLeMindCount(leMindSize.intValue());

      BigDecimal totalBonusAmountByMind = new BigDecimal("0");
      BigDecimal leScorePer = new BigDecimal(leScorePerConfig.getConfigValue());
      List<EndUser> userList = new ArrayList<EndUser>();
      List<LeScoreRecord> leScoreList = new ArrayList<LeScoreRecord>();
      List<LeBeanRecord> leBeanList = new ArrayList<LeBeanRecord>();
      for (LeMindRecord leMindRecord : leMindRecords) {
        EndUser endUser = leMindRecord.getEndUser();
        BigDecimal curBonus = leMindRecord.getAmount().multiply(value);
        BigDecimal totalLeScoreBonus = leMindRecord.getTotalBonus().add(curBonus);



        if (totalLeScoreBonus.compareTo(leMindRecord.getMaxBonus()) >= 0) {
          curBonus = leMindRecord.getMaxBonus().subtract(leMindRecord.getTotalBonus());
          leMindRecord.setTotalBonus(leMindRecord.getMaxBonus());
          leMindRecord.setStatus(CommonStatus.INACTIVE);

          LeMindRecord inactiveRecord = new LeMindRecord();
          inactiveRecord.setEndUser(endUser);
          inactiveRecord.setAmount(leMindRecord.getAmount().negate());
          inactiveRecord.setUserCurLeMind(endUser.getCurLeMind().add(leMindRecord.getAmount()));
          inactiveRecord.setRemark("分红满限额扣除乐心");
          endUser.getLeMindRecords().add(inactiveRecord);

          endUser.setCurLeMind(endUser.getCurLeMind().subtract(leMindRecord.getAmount()));
        } else {
          leMindRecord.setTotalBonus(totalLeScoreBonus);
        }

        BonusByMindPerDay bonusByMindPerDay = new BonusByMindPerDay();
        bonusByMindPerDay.setLeMindRecord(leMindRecord);
        bonusByMindPerDay.setBonusDate(startTime);
        bonusByMindPerDay.setBonusAmount(curBonus);
        leMindRecord.getBonusByDays().add(bonusByMindPerDay);


        if (leScorePer.compareTo(new BigDecimal("0")) > 0) {
          BigDecimal leScoreBonus = curBonus.multiply(leScorePer);
          LeScoreRecord leScoreRecord = compareEndUserLeScore(leScoreList, endUser);
          if (leScoreRecord != null) {
            leScoreRecord.setAmount(leScoreRecord.getAmount().add(leScoreBonus));
            leScoreRecord.setUserCurLeScore(endUser.getCurLeScore().add(leScoreBonus));
            endUser.setMotivateLeScore(endUser.getMotivateLeScore().add(leScoreBonus));
            endUser.setCurLeScore(leScoreRecord.getUserCurLeScore());
            endUser.setTotalLeScore(endUser.getTotalLeScore().add(leScoreBonus));

          } else {
            leScoreRecord = new LeScoreRecord();
            leScoreRecord.setEndUser(endUser);
            leScoreRecord.setLeScoreType(LeScoreType.BONUS);
            leScoreRecord.setAmount(leScoreBonus);
            leScoreRecord.setUserCurLeScore(endUser.getCurLeScore().add(leScoreRecord.getAmount()));
            endUser.getLeScoreRecords().add(leScoreRecord);
            endUser.setMotivateLeScore(endUser.getMotivateLeScore().add(leScoreRecord.getAmount()));
            endUser.setCurLeScore(leScoreRecord.getUserCurLeScore());
            endUser.setTotalLeScore(endUser.getTotalLeScore().add(leScoreRecord.getAmount()));
          }
          leScoreList.add(leScoreRecord);
        }

        if (leScorePer.compareTo(new BigDecimal("1")) < 0) {
          BigDecimal leBeanBonus = curBonus.subtract(curBonus.multiply(leScorePer));
          LeBeanRecord leBeanRecord = compareEndUserLeBean(leBeanList, endUser);
          if (leBeanRecord != null) {
            leBeanRecord.setAmount(leBeanRecord.getAmount().add(leBeanBonus));
            leBeanRecord.setUserCurLeBean(endUser.getCurLeBean().add(leBeanBonus));
            endUser.setCurLeBean(leBeanRecord.getUserCurLeBean());
            endUser.setTotalLeBean(endUser.getTotalLeBean().add(leBeanBonus));
          } else {
            leBeanRecord = new LeBeanRecord();
            leBeanRecord.setAmount(leBeanBonus);
            // leBeanRecord.setAmount(curBonus);
            leBeanRecord.setEndUser(endUser);
            leBeanRecord.setType(LeBeanChangeType.BONUS);
            leBeanRecord.setUserCurLeBean(endUser.getCurLeBean().add(leBeanRecord.getAmount()));
            endUser.getLeBeanRecords().add(leBeanRecord);
            endUser.setCurLeBean(leBeanRecord.getUserCurLeBean());
            endUser.setTotalLeBean(endUser.getTotalLeBean().add(leBeanRecord.getAmount()));
          }
          leBeanList.add(leBeanRecord);
        }


        totalBonusAmountByMind = totalBonusAmountByMind.add(curBonus);
        userList.add(endUser);
      }

      bonusParamPerDay.setBonusAmount(totalBonusAmountByMind.toString());

      if (LogUtil.isDebugEnabled(EndUserServiceImpl.class)) {
        LogUtil
            .debug(
                EndUserServiceImpl.class,
                "dailyBonusCalJob",
                "daily Bonus calculate job processing--Update User Bonus Info==========start=========. Timer Period: %s,totalBonus: %s,value: %s",
                startTime + "-" + endTime, totalBonus, value);
      }

      endUserDao.merge(userList);

      if (LogUtil.isDebugEnabled(EndUserServiceImpl.class)) {
        LogUtil
            .debug(
                EndUserServiceImpl.class,
                "dailyBonusCalJob",
                "daily Bonus calculate job processing--Update User Bonus Info==========end=========. Timer Period: %s",
                startTime + "-" + endTime);
      }


      msg =
          "每日分红运算成功!\n服务器地址:" + setting.getServerIp() + "\n日期:"
              + TimeUtils.format("yyyy-MM-dd", startTime.getTime()) + "\n当日平台收益总金额：" + totalBonus
              + "\n当日平台分红参数value值：" + value + "\n当日平台分红总乐豆：" + totalBonusAmountByMind;
      bonusParamPerDay.setRemark("每日分红运算成功!");
      // mailService.send("sujinxuan123@163.com,sj_msc@163.com",
      // "yxsh:daily bonus calculate job successfully!", "服务器地址:" + setting.getServerIp()
      // + "\n日期:" + startTime + "\n当日平台用于分红的总金额：" + totalBonus + "\n当日消费产生乐心大于等于1的用户数量："
      // + endUsers.size() + "\n当日平台分红参数value值：" + value);
    } catch (Exception e) {
      msg = "每日分红运算失败!\nRuntime Exception:" + e.getMessage();
      bonusParamPerDay.setRemark(msg);
      // mailService.send("sujinxuan123@163.com,sj_msc@163.com",
      // "yxsh:daily bonus calculate 每日分红运算失败!", e.getMessage());
    } finally {
      bonusParamPerDayDao.persist(bonusParamPerDay);
      mailService.send(emailTo, subject, msg);
    }


  }

  private LeScoreRecord compareEndUserLeScore(List<LeScoreRecord> leScoreRecords, EndUser endUser) {
    LeScoreRecord leScoreRecord = null;
    for (LeScoreRecord record : leScoreRecords) {
      if (endUser.getId().equals(record.getEndUser().getId())) {
        leScoreRecord = record;
        break;
      }
    }
    return leScoreRecord;
  }

  private LeBeanRecord compareEndUserLeBean(List<LeBeanRecord> leBeanRecords, EndUser endUser) {
    LeBeanRecord leBeanRecord = null;
    for (LeBeanRecord record : leBeanRecords) {
      if (endUser.getId().equals(record.getEndUser().getId())) {
        leBeanRecord = record;
        break;
      }
    }
    return leBeanRecord;
  }

  @Override
  public EndUser findByUserName(String userName) {
    return null;
  }

  @Override
  public EndUser findByUserMobile(String mobileNo) {
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


  @Override
  public List<EndUser> getMindUsersByDay(Date startTime, Date endTime) {
    return endUserDao.getMindUsersByDay(startTime, endTime);
  }


  @Override
  public EndUser getAgentByArea(Area area) {
    return endUserDao.getAgentByArea(area);
  }



}

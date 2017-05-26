package org.rebate.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.rebate.beans.Message;
import org.rebate.dao.SystemConfigDao;
import org.rebate.entity.SystemConfig;
import org.rebate.entity.commonenum.CommonEnum.SystemConfigKey;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.service.SystemConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("systemConfigServiceImpl")
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfig, Long> implements
    SystemConfigService {

  @Resource(name = "systemConfigDaoImpl")
  private SystemConfigDao systemConfigDao;

  @Resource(name = "systemConfigDaoImpl")
  public void setBaseDao(SystemConfigDao systemConfigDao) {
    super.setBaseDao(systemConfigDao);
  }

  @Override
  public SystemConfig getConfigByKey(SystemConfigKey key) {
    return systemConfigDao.getConfigByKey(key);
  }
  
  @Override
  public SystemConfig getConfigByKeyIgnoreIsEnabled(SystemConfigKey key) {
    return systemConfigDao.getConfigByKeyIgnoreIsEnabled(key);
  }

/*  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public SystemConfig update(SystemConfig entity) {
    if (SystemConfigKey.TRANSACTION_FEE_PERCENTAGE.equals(entity.getConfigKey())
        && entity.getIsEnabled()) {
      SystemConfig config = getConfigByKey(SystemConfigKey.TRANSACTION_FEE_PERTIME);
      if (config != null && config.getIsEnabled()) {
        config.setIsEnabled(false);
        super.update(config);
      }
    } else if (SystemConfigKey.TRANSACTION_FEE_PERTIME.equals(entity.getConfigKey())
        && entity.getIsEnabled()) {
      SystemConfig config = getConfigByKey(SystemConfigKey.TRANSACTION_FEE_PERCENTAGE);
      if (config != null && config.getIsEnabled()) {
        config.setIsEnabled(false);
        super.update(config);
      }
    }
    return super.update(entity);
  }*/

  @Override
  //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Message updateSystemConfig(SystemConfig entity) {
    if (SystemConfigKey.TRANSACTION_FEE_PERCENTAGE.equals(entity.getConfigKey())) {
      SystemConfig config = getConfigByKeyIgnoreIsEnabled(SystemConfigKey.TRANSACTION_FEE_PERTIME);
      if (config != null && entity.getIsEnabled()&& config.getIsEnabled()) {
        config.setIsEnabled(false);
        super.update(config);
      }else if (config != null && !entity.getIsEnabled()&& !config.getIsEnabled()) {
        return Message.error("rebate.systemConfig.TRANSACTION_FEE.cannot.disabled");
      }
    } else if (SystemConfigKey.TRANSACTION_FEE_PERTIME.equals(entity.getConfigKey())
        && entity.getIsEnabled()) {
      SystemConfig config = getConfigByKeyIgnoreIsEnabled(SystemConfigKey.TRANSACTION_FEE_PERCENTAGE);
      if (config != null && entity.getIsEnabled() &&config.getIsEnabled()) {
        config.setIsEnabled(false);
        super.update(config);
      }else if (config != null && !entity.getIsEnabled()&& !config.getIsEnabled()) {
        return Message.error("rebate.systemConfig.TRANSACTION_FEE.cannot.disabled");
      }
    } else if (SystemConfigKey.RECOMMEND_LEVEL_LIMIT.equals(entity.getConfigKey())) {
      if(!entity.getIsEnabled()){
        return Message.error("rebate.systemConfig.cannot.disabled");
      }
      // 消费用户的间接推荐人收益百分比
      SystemConfig indirect = getConfigByKey(SystemConfigKey.RECOMMEND_INDIRECT_USER);
      // 消费用户的直接推荐人收益百分比
      SystemConfig direct = getConfigByKey(SystemConfigKey.RECOMMEND_DIRECT_USER);
      // 分享佣金占总让利百分比
      commissionCalculate(indirect,direct,entity);
    } else if (SystemConfigKey.RECOMMEND_INDIRECT_USER.equals(entity.getConfigKey())) {
      if(!entity.getIsEnabled()){
        return Message.error("rebate.systemConfig.cannot.disabled");
      }
      // 消费用户的间接推荐人收益百分比
      SystemConfig limit = getConfigByKey(SystemConfigKey.RECOMMEND_LEVEL_LIMIT);
      // 消费用户的直接推荐人收益百分比
      SystemConfig direct = getConfigByKey(SystemConfigKey.RECOMMEND_DIRECT_USER);
      // 分享佣金占总让利百分比
      commissionCalculate(entity,direct,limit);
    } else if (SystemConfigKey.RECOMMEND_DIRECT_USER.equals(entity.getConfigKey())) {
      if(!entity.getIsEnabled()){
        return Message.error("rebate.systemConfig.cannot.disabled");
      }
      // 消费用户的间接推荐人收益百分比
      SystemConfig indirect = getConfigByKey(SystemConfigKey.RECOMMEND_INDIRECT_USER);
      // 消费用户的直接推荐人收益百分比
      SystemConfig limit = getConfigByKey(SystemConfigKey.RECOMMEND_LEVEL_LIMIT);
      // 分享佣金占总让利百分比
      commissionCalculate(indirect,entity,limit);
    }
    super.update(entity);
    return Message.success("rebate.message.success");
  }

  private void commissionCalculate(SystemConfig indirect, SystemConfig direct, SystemConfig limit) {
    BigDecimal commissionValue = new BigDecimal(0);
    BigDecimal indirectValue = new BigDecimal(indirect.getConfigValue());
    BigDecimal directValue = new BigDecimal(direct.getConfigValue());
    BigDecimal limitValue = new BigDecimal(limit.getConfigValue());
    if (limitValue.compareTo(new BigDecimal(1)) == 0) {
      commissionValue = directValue;
    } else if (limitValue.compareTo(new BigDecimal(1)) == 1) {
      // 算法 commission = direct + （limit - 1）* indirect;
      commissionValue =
          directValue.add(limitValue.subtract(new BigDecimal(1)).multiply(indirectValue));
    }
    SystemConfig commission =
        getConfigByKey(SystemConfigKey.USER_RECOMMEND_COMMISSION_PERCENTAGE);
    commission.setConfigValue(commissionValue.toString());
    super.update(commission);
  }


}

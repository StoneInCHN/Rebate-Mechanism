package org.rebate.job;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

/**
 * JobFactory quartz读取Spring Bean的配置。如无特殊需求，不需要修改
 * 
 * @author huyong
 *
 */
public class JobFactory extends AdaptableJobFactory {

  @Autowired
  private AutowireCapableBeanFactory capableBeanFactory;

  @Override
  protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
    // 调用父类的方法
    Object jobInstance = super.createJobInstance(bundle);
    // 进行注入
    capableBeanFactory.autowireBean(jobInstance);
    return jobInstance;
  }

}

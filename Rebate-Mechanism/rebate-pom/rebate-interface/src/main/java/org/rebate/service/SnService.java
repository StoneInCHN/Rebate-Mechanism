package org.rebate.service;

import org.rebate.entity.EndUser;
import org.rebate.entity.Sn.Type;
import org.rebate.framework.service.BaseService;

public interface SnService extends BaseService<EndUser, Long> {

  /**
   * 生成序列号
   * 
   * @param type 类型
   * @return 序列号
   */
  String generate(Type type);
}

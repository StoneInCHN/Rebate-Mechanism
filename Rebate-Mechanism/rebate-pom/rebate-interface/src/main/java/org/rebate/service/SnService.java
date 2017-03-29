package org.rebate.service;

import org.rebate.entity.Sn.Type;

public interface SnService {

  /**
   * 生成序列号
   * 
   * @param type 类型
   * @return 序列号
   */
  String generate(Type type);
}

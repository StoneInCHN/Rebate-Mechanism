package org.rebate.service;

import java.util.List;

import org.rebate.beans.LogConfig;

/**
 * 获取日志配置
 * 
 *
 */
public interface LogConfigService {
  /**
   * 获取所有日志配置
   * 
   * @return 所有日志配置
   */
  List<LogConfig> getAll();
}

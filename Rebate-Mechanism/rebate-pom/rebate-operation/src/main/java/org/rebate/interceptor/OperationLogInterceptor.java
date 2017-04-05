package org.rebate.interceptor;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.rebate.beans.LogConfig;
import org.rebate.entity.OperationLog;
import org.rebate.service.AdminService;
import org.rebate.service.LogConfigService;
import org.rebate.service.OperationLogService;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 * /** 后台操作日志拦截器
 * 
 * @author shijun
 */
public class OperationLogInterceptor extends HandlerInterceptorAdapter {

  /** 默认忽略参数 */
  private static final String[] DEFAULT_IGNORE_PARAMETERS = new String[] {"password", "rePassword",
      "currentPassword"};

  /** 忽略参数 */
  private String[] ignoreParameters = DEFAULT_IGNORE_PARAMETERS;

  /** antPathMatcher */
  private static AntPathMatcher antPathMatcher = new AntPathMatcher();

  @Resource(name = "logConfigServiceImpl")
  private LogConfigService logConfigService;

  @Resource(name = "adminServiceImpl")
  private AdminService adminService;

  @Resource(name = "operationLogServiceImpl")
  private OperationLogService operationLogService;

  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    List<LogConfig> logConfigs = logConfigService.getAll();
    if (logConfigs != null) {
      String path = request.getServletPath();
      for (LogConfig logConfig : logConfigs) {
        if (antPathMatcher.match(logConfig.getUrlPattern(), path)) {
          String username = adminService.getCurrentUsername();
          String operation = logConfig.getOperation();
          String operator = username;
          String content = (String) request.getAttribute(OperationLog.LOG_CONTENT_ATTRIBUTE_NAME);
          String ip = request.getRemoteAddr();
          request.removeAttribute(OperationLog.LOG_CONTENT_ATTRIBUTE_NAME);
          StringBuffer parameter = new StringBuffer();
          Map<String, String[]> parameterMap = request.getParameterMap();
          if (parameterMap != null) {
            for (Entry<String, String[]> entry : parameterMap.entrySet()) {
              String parameterName = entry.getKey();
              if (!ArrayUtils.contains(ignoreParameters, parameterName)) {
                String[] parameterValues = entry.getValue();
                if (parameterValues != null) {
                  for (String parameterValue : parameterValues) {
                    parameter.append(parameterName + " = " + parameterValue + "\n");
                  }
                }
              }
            }
          }
          OperationLog log = new OperationLog();
          log.setOperation(operation);
          log.setOperator(operator);
          log.setContent(content);
          log.setParameter(parameter.toString());
          log.setIp(ip);
          operationLogService.save(log);
          break;
        }
      }
    }
  }
}

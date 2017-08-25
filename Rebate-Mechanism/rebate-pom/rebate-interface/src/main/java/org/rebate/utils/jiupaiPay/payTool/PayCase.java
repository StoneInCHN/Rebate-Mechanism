package org.rebate.utils.jiupaiPay.payTool;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.rebate.beans.Setting;
import org.rebate.utils.SettingUtils;
import org.rebate.utils.jiupaiPay.utils.MerchantUtil;
import org.rebate.utils.jiupaiPay.utils.RSASignUtil;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;


public abstract class PayCase {
  private static String encoding;
  private static String requestUrl;
  private static String merchantCertPath;
  private static String merchantCertPass;
  private static String rootCertPath;
  private static final Logger log = (Logger) LoggerFactory.getLogger(PayCase.class);
  private static Setting setting = SettingUtils.get();

  public PayCase() {

  }

  public void init(Map<String, String> requestMap) {
    // 获取项目根路径
    String path = this.getClass().getResource("/").getPath();
    String merchantId = setting.getJiupaiMerchantId();
    String merCert = merchantId + ".p12";
    merchantCertPath = path + File.separator + setting.getJiupaiMerchantCertPath() + merCert;
    // log.debug(merchantCertPath);
    // merchantCertPath = "D:\\800002308510001.p12";
    merchantCertPass = setting.getJiupaiMerchantCertPass();
    rootCertPath = path + File.separator + setting.getJiupaiRootCertPath();
    // log.debug(rootCertPath);
    requestUrl = setting.getJiupaiRequestUrl();
    String charset = setting.getJiupaiCharset();
    String version = setting.getJiupaiVersion();
    String signType = setting.getJiupaiSignType();

    // 取得商户当前系统时间
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    // 请求时间
    String requestTime = sdf.format(new Date());
    String requestId = String.valueOf(System.currentTimeMillis());
    // 编码设置
    if ("00".equals(charset)) {
      encoding = "GB18030";
    } else if ("01".equals(charset)) {
      encoding = "GB2312";
    } else if ("02".equals(charset)) {
      encoding = "UTF-8";
    } else {
      encoding = "GBK";
    }
    // 公共请求参数
    requestMap.put("charset", charset);
    requestMap.put("version", version);
    requestMap.put("signType", signType);
    requestMap.put("merchantId", merchantId);
    requestMap.put("requestTime", requestTime);
    requestMap.put("requestId", requestId);
  }

  public String bizService(Map<String, String> requestMap) {
    // 添加公共参数
    init(requestMap);
    //
    Map dataMap = new HashMap();
    try {
      dataMap.putAll(requestMap);
      Set set = requestMap.keySet();
      Iterator iterator = set.iterator();
      while (iterator.hasNext()) {
        String key = (String) iterator.next();
        if ((requestMap.get(key) == null) || requestMap.get(key).toString().trim().length() == 0) {
          dataMap.remove(key);
        }
      }
      RSASignUtil util = new RSASignUtil(merchantCertPath, merchantCertPass);
      String reqData = util.coverMap2String(dataMap);
      String merchantSign = util.sign(reqData, encoding);
      String merchantCert = util.getCertInfo();
      // -- 请求报文
      String buf = reqData + "&merchantSign=" + merchantSign + "&merchantCert=" + merchantCert;
      // 调用http
      String res = MerchantUtil.sendAndRecv(requestUrl, buf, requestMap.get("charset"));
      log.info("远程调用返回的报文:{}", res);
      return res;
    } catch (Exception e) {
      log.error("签名异常", e);
    }
    return null;
  }

  public Map<String, String> validRetSign(Map<String, String> retMap, String res) {
    // 公共参数，具体业务签名参数由调用方传入
    retMap.put("charset", RSASignUtil.getValue(res, "charset"));
    retMap.put("version", RSASignUtil.getValue(res, "version"));
    retMap.put("signType", RSASignUtil.getValue(res, "signType"));
    retMap.put("merchantId", RSASignUtil.getValue(res, "merchantId"));
    retMap.put("rspCode", RSASignUtil.getValue(res, "rspCode"));
    retMap.put("rspMessage", RSASignUtil.getValue(res, "rspMessage"));
    retMap.put("requestId", RSASignUtil.getValue(res, "requestId"));
    retMap.put("responseId", RSASignUtil.getValue(res, "responseId"));
    retMap.put("responseTime", RSASignUtil.getValue(res, "responseTime"));
    if (retMap.get("rspCode").equals("IPS00020")) {// 网关dubbo异常后，默认的charset为00
      encoding = "GB18030";
    }
    Map responseMap = new HashMap();
    responseMap.putAll(retMap);
    Set set1 = retMap.keySet();
    Iterator iterator1 = set1.iterator();
    while (iterator1.hasNext()) {
      String key0 = (String) iterator1.next();
      String tmp = retMap.get(key0);
      if (StringUtils.equals(tmp, "null") || StringUtils.isBlank(tmp)) {
        responseMap.remove(key0);
      }
    }
    String sf = RSASignUtil.coverMap2String(responseMap);
    // -- 验证签名
    boolean flag = false;
    RSASignUtil rsautil = new RSASignUtil(rootCertPath);
    try {
      flag =
          rsautil.verify(sf, RSASignUtil.getValue(res, "serverSign"),
              RSASignUtil.getValue(res, "serverCert"), encoding);
      if (flag) {
        log.info("返回签名验签成功");
        retMap.put("verifySign", "success");
      } else {
        log.info("返回签名验签失败");
        retMap.put("verifySign", "fail");
      }
    } catch (Exception e) {
      log.error("返回签名验签异常", e);
      retMap.put("verifySign", "error");
    }
    return retMap;
  }
}

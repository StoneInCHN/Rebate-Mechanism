package org.rebate.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.rebate.common.log.LogUtil;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.service.OrderService;
import org.rebate.utils.alipay.util.AlipayNotify;
import org.rebate.utils.allinpay.PayNotify;
import org.rebate.utils.wechat.WeixinUtil;
import org.rebate.utils.yipay.CryptTool;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller("notifyController")
@RequestMapping("/payNotify")
public class NotifyController extends MobileBaseController {


  @Resource(name = "orderServiceImpl")
  private OrderService orderService;

  @Resource(name = "taskExecutor")
  private TaskExecutor taskExecutor;

  // /**
  // * 通联支付回调接口
  // *
  // * @param req
  // * @return
  // * @throws IOException
  // */
  // @RequestMapping(value = "/notify_allinpay", method = RequestMethod.POST)
  // public @ResponseBody String notify_allinpay(HttpServletRequest request) throws Exception {
  // // 获取支付宝POST过来反馈信息
  // Map<String, String> params = new LinkedHashMap<String, String>();
  // Map requestParams = request.getParameterMap();
  // for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
  // String name = (String) iter.next();
  // String[] values = (String[]) requestParams.get(name);
  // String valueStr = "";
  // for (int i = 0; i < values.length; i++) {
  // valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
  // }
  // // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
  // // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
  // params.put(name, valueStr);
  // }
  // if (LogUtil.isDebugEnabled(NotifyController.class)) {
  // LogUtil.debug(NotifyController.class, "notify_allinpay",
  // "allin pay notify callback method. response: %s", params);
  // }
  //
  //
  // // 商户订单号
  // String out_trade_no =
  // new String(request.getParameter("orderNo").getBytes("ISO-8859-1"), "UTF-8");
  // // 交易金额
  // String total_fee =
  // new String(request.getParameter("payAmount").getBytes("ISO-8859-1"), "UTF-8");
  // // 加密类型
  // String sign_type = new String(request.getParameter("signType").getBytes("ISO-8859-1"),
  // "UTF-8");
  // if ("0".equals(sign_type) && PayNotify.verify(params)) {// sign_type:0 MD5加密 sign_type:1 RSA加密
  // if (LogUtil.isDebugEnabled(NotifyController.class)) {
  // LogUtil
  // .debug(
  // NotifyController.class,
  // "notify_allinpay",
  // "user pay order call back successfully with allin pay. orderSn: %s, amount: %s,sign_type: %s",
  // out_trade_no, total_fee, sign_type);
  // }
  // orderService.updateOrderforPayCallBack(out_trade_no);
  // return "success";
  // } else {// 验证失败
  // if (LogUtil.isDebugEnabled(NotifyController.class)) {
  // LogUtil
  // .debug(
  // NotifyController.class,
  // "notify_allinpay",
  // "user pay order call back verify sign failed with allin pay. orderSn: %s,sign_type: %s",
  // out_trade_no, sign_type);
  // }
  // return "fail";
  // }
  // }

  /**
   * 通联H5支付回调接口
   * 
   * @param req
   * @return
   * @throws IOException
   */
  @RequestMapping(value = "/notify_allinpay_H5", method = RequestMethod.POST)
  public @ResponseBody String notify_allinpay_H5(HttpServletRequest request) throws Exception {
    // 获取支付宝POST过来反馈信息
    Map<String, String> params = new HashMap<String, String>();
    Map requestParams = request.getParameterMap();
    for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
      String name = (String) iter.next();
      String[] values = (String[]) requestParams.get(name);
      String valueStr = "";
      for (int i = 0; i < values.length; i++) {
        valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
      }
      // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
      // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
      params.put(name, valueStr);
    }
    if (LogUtil.isDebugEnabled(NotifyController.class)) {
      LogUtil.debug(NotifyController.class, "notify_allinpay_H5",
          "allin pay H5 notify callback method. response: %s", params);
    }

    // 商户订单号
    String out_trade_no =
        new String(request.getParameter("orderNo").getBytes("ISO-8859-1"), "UTF-8");
    // 交易金额
    String total_fee =
        new String(request.getParameter("payAmount").getBytes("ISO-8859-1"), "UTF-8");
    // 加密类型
    String sign_type = new String(request.getParameter("signType").getBytes("ISO-8859-1"), "UTF-8");
    if ("0".equals(sign_type) && PayNotify.verifySignH5(params)) {// sign_type:0 MD5加密 sign_type:1
                                                                  // RSA加密
      if (LogUtil.isDebugEnabled(NotifyController.class)) {
        LogUtil
            .debug(
                NotifyController.class,
                "notify_allinpay_H5",
                "user pay order call back successfully with allin pay H5. orderSn: %s, amount: %s,sign_type: %s",
                out_trade_no, total_fee, sign_type);
      }
      taskExecutor.execute(new Runnable() {
        public void run() {
          orderService.updateOrderforPayCallBack(out_trade_no);
        }
      });

    } else {// 验证失败
      if (LogUtil.isDebugEnabled(NotifyController.class)) {
        LogUtil
            .debug(
                NotifyController.class,
                "notify_allinpay_H5",
                "user pay order call back verify sign failed with allin pay H5. orderSn: %s,sign_type: %s",
                out_trade_no, sign_type);
      }
    }
    return "success";
  }

  /**
   * 翼支付回调接口
   * 
   * @param req
   * @return
   * @throws IOException
   */
  @RequestMapping(value = "/notify_yipay", method = RequestMethod.POST)
  public @ResponseBody String notify_yipay(HttpServletRequest request) throws Exception {
    String uptranSeq = request.getParameter("UPTRANSEQ");// 翼支付网关平台交易流水号
    String trandate = request.getParameter("TRANDATE");// 翼支付网关平台交易日期
    String retncode = request.getParameter("RETNCODE");// 处理结果码
    String retninfo = request.getParameter("RETNINFO");// 处理结果解释码
    String orderreqtranseq = request.getParameter("ORDERREQTRANSEQ");// 订单请求交易流水号
    String orderseq = request.getParameter("ORDERSEQ");// 订单号
    String orderamount = request.getParameter("ORDERAMOUNT");// 订单总金额
    // String productamount = request.getParameter("PRODUCTAMOUNT");
    // String attachamount = request.getParameter("ATTACHAMOUNT");
    // String curtype = request.getParameter("CURTYPE");
    String encodetype = request.getParameter("ENCODETYPE");// 加密方式0：不加密 1：MD5摘要(默认)
    // String attach = request.getParameter("ATTACH");
    String sign = request.getParameter("SIGN");// 数字签名
    String merchantId = request.getParameter("MERCHANTID");// 商户号
    if (LogUtil.isDebugEnabled(NotifyController.class)) {
      LogUtil
          .debug(
              NotifyController.class,
              "notify_yipay",
              "yi pay notify callback method. response: UPTRANSEQ: %s,RETNCODE: %s,RETNINFO: %s,ORDERSEQ: %s,ORDERAMOUNT: %s,ENCODETYPE: %s,SIGN: %s",
              uptranSeq, retncode, retninfo, orderseq, orderamount, encodetype, sign);
    }
    String check =
        "UPTRANSEQ=" + uptranSeq + "&MERCHANTID=" + merchantId + "&ORDERSEQ=" + orderseq
            + "&ORDERAMOUNT=" + orderamount + "&RETNCODE=" + retncode + "&RETNINFO=" + retninfo
            + "&TRANDATE=" + trandate + "&KEY=" + setting.getYiMerchantKey();
    String checkMac = CryptTool.md5Digest(check);
    if (checkMac.equals(sign)) {
      if ("0000".equals(retncode)) {
        if (LogUtil.isDebugEnabled(NotifyController.class)) {
          LogUtil.debug(NotifyController.class, "notify_yipay",
              "user pay order call back successfully with yi pay. orderSn: %s, amount: %s,",
              orderseq, orderamount);
        }
        taskExecutor.execute(new Runnable() {
          public void run() {
            orderService.updateOrderforPayCallBack(orderseq);
          }
        });
        // orderService.updateOrderforPayCallBack(orderseq);
      } else {
        /**
         * 支付结果不成功：结果码为“0000”表示支付成功，其他值则表示支付失败
         */
        if (LogUtil.isDebugEnabled(NotifyController.class)) {
          LogUtil
              .debug(
                  NotifyController.class,
                  "notify_yipay",
                  "yi pay notify callback method. pay result code is failed. retncode: %s,retninfo: %s,orderSn: %s",
                  retncode, retninfo, orderseq);
        }
      }
    } else {
      /**
       * 签名验证失败
       */
      if (LogUtil.isDebugEnabled(NotifyController.class)) {
        LogUtil
            .debug(
                NotifyController.class,
                "notify_yipay",
                "yi pay notify callback method. sign verify failed. yiPaySign: %s,checkStr: %s,checkSign: %s",
                sign, check, checkMac);
      }
    }

    return "UPTRANSEQ_" + uptranSeq;
  }


  /**
   * 微信支付回调接口
   * 
   * @param req
   * @return
   * @throws IOException
   */
  @RequestMapping(value = "/notify_wechat", method = RequestMethod.POST)
  public @ResponseBody String notify_wechat(HttpServletRequest request) throws Exception {
    // 获取支付通知xml数据
    ServletInputStream inputStream = request.getInputStream();
    InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
    BufferedReader bufferReader = new BufferedReader(reader);
    String xml = null;
    StringBuffer buffer = new StringBuffer();
    while ((xml = bufferReader.readLine()) != null) {
      buffer.append(xml);
    }
    bufferReader.close();
    reader.close();
    inputStream.close();
    inputStream = null;
    // 解析xml数据
    Map<String, Object> xmlMap = new HashMap<String, Object>();
    // 解析XML数据
    Document doc = DocumentHelper.parseText(buffer.toString());
    Element root = doc.getRootElement();
    Iterator it = root.elementIterator();
    while (it.hasNext()) {
      Element temp = (Element) it.next();
      xmlMap.put(temp.getName(), temp.getStringValue());
    }
    if (LogUtil.isDebugEnabled(NotifyController.class)) {
      LogUtil.debug(NotifyController.class, "notify_wechat",
          "wechat pay notify callback method. response: %s", xmlMap);
    }
    String xmlReturn = "";
    if ("SUCCESS".equals(xmlMap.get("return_code"))) {
      // 验证签名
      String sign = xmlMap.get("sign") + "";
      xmlMap.put("sign", "");
      if (sign.equals(WeixinUtil.getSign(xmlMap))) {
        // 查询是否处理过
        // 处理回调结果
        if ("SUCCESS".equals(xmlMap.get("result_code"))) {
          // 系统订单编号
          String out_trade_no = xmlMap.get("out_trade_no").toString();
          // 支付的金额
          String total_fee = xmlMap.get("total_fee").toString();
          // 货币种类
          String fee_type = xmlMap.get("fee_type").toString();
          // 微信支付订单号
          String transaction_id = xmlMap.get("transaction_id").toString();

          String payment = "wx";

          BigDecimal amount = new BigDecimal(total_fee).divide(new BigDecimal(100));
          if (LogUtil.isDebugEnabled(NotifyController.class)) {
            LogUtil.debug(NotifyController.class, "notify_wechat",
                "user pay order call back successfully with wechat pay. orderSn: %s, amount: %s,",
                out_trade_no, amount);
          }
          taskExecutor.execute(new Runnable() {
            public void run() {
              orderService.updateOrderforPayCallBack(out_trade_no);
            }
          });

        } else {
          if (LogUtil.isDebugEnabled(NotifyController.class)) {
            LogUtil.debug(NotifyController.class, "notify_wechat", "WeChat pay fail. orderSn: %s",
                xmlMap.get("out_trade_no").toString());
          }
        }
      }
      // 返回处理结果xml
      xmlReturn =
          "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    } else {
      // 返回处理结果xml
      xmlReturn = "<xml><return_code>FAIL</return_code><return_msg>签名错误</return_msg></xml>";
    }

    return xmlReturn;
  }


  /**
   * 支付宝支付回调接口
   * 
   * @param req
   * @return
   * @throws IOException
   */
  @RequestMapping(value = "/notify_alipay", method = RequestMethod.POST)
  public @ResponseBody String notify_alipay(HttpServletRequest request) throws Exception {
    // 获取支付宝POST过来反馈信息
    Map<String, String> params = new HashMap<String, String>();
    Map requestParams = request.getParameterMap();
    for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
      String name = (String) iter.next();
      String[] values = (String[]) requestParams.get(name);
      String valueStr = "";
      for (int i = 0; i < values.length; i++) {
        valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
      }
      // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
      // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
      params.put(name, valueStr);
    }
    if (LogUtil.isDebugEnabled(NotifyController.class)) {
      LogUtil.debug(NotifyController.class, "notify_alipay",
          "Alipay pay notify callback method. response: %s", params);
    }

    // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
    // 商户订单号
    String out_trade_no =
        new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
    // 支付宝交易号
    String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
    // 交易状态
    String trade_status =
        new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
    // 交易金额
    String total_fee =
        new String(request.getParameter("total_fee").getBytes("ISO-8859-1"), "UTF-8");
    // 购买者id
    String buyer_id = new String(request.getParameter("buyer_id").getBytes("ISO-8859-1"), "UTF-8");
    // 购买中邮箱
    String buyer_eamil =
        new String(request.getParameter("buyer_email").getBytes("ISO-8859-1"), "UTF-8");
    if (AlipayNotify.verify(params)) {// 验证成功
      // ////////////////////////////////////////////////////////////////////////////////////////
      // 请在这里加上商户的业务逻辑程序代码
      // ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
      if (trade_status.equals("TRADE_FINISHED")) {
        // 判断该笔订单是否在商户网站中已经做过处理
        // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
        // 如果有做过处理，不执行商户的业务程序
        String payment = "alipay";
        alipayNotifySuccess(out_trade_no, total_fee, trade_status);
        // 注意：
        // 该种交易状态只在两种情况下出现
        // 1、开通了普通即时到账，买家付款成功后。
        // 2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
      } else if (trade_status.equals("TRADE_SUCCESS")) {
        // 判断该笔订单是否在商户网站中已经做过处理
        // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
        // 如果有做过处理，不执行商户的业务程序
        String payment = "alipay";
        alipayNotifySuccess(out_trade_no, total_fee, trade_status);

        // 注意：
        // 该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
      }

      // ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

      return "success"; // 请不要修改或删除

      // ////////////////////////////////////////////////////////////////////////////////////////
    } else {// 验证失败
      return "fail";
    }
  }

  private void alipayNotifySuccess(String out_trade_no, String total_fee, String trade_status) {
    // BigDecimal amount = new BigDecimal(total_fee);

    if (LogUtil.isDebugEnabled(NotifyController.class)) {
      LogUtil
          .debug(
              NotifyController.class,
              "notify_alipay",
              "user pay order call back successfully with alipay. orderSn: %s, amount: %s, trade_status: %s",
              out_trade_no, total_fee, trade_status);
    }
    taskExecutor.execute(new Runnable() {
      public void run() {
        orderService.updateOrderforPayCallBack(out_trade_no);
      }
    });
    // orderService.updateOrderforPayCallBack(out_trade_no);
  }
}

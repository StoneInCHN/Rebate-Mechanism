package org.rebate.beans;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;


/**
 * 系统设置
 * 
 */
public class Setting implements Serializable {

  private static final long serialVersionUID = -1478999889661796840L;

  /**
   * 验证码类型
   */
  public enum CaptchaType {

    /** 后台登录 */
    adminLogin,

    /** 租户资料上传 */
    adminApply,

    /** 找回密码 */
    findPassword,

    /** 重置密码 */
    resetPassword,

    /** 其它 */
    other
  }

  public enum ImageType {
    /**
     * 营业执照
     */
    LICENSE,
    /**
     * 门店照片
     */
    STOREPICTURE,
    /**
     * 广告
     */
    ADVERTISEMENT,
    /**
     * 商家类型
     */
    SELLERCATEGORY
  }


  /** 缓存名称 */
  public static final String CACHE_NAME = "setting";

  /** 缓存Key */
  public static final Integer CACHE_KEY = 0;

  /** 分隔符 */
  private static final String SEPARATOR = ",";


  /** 密码最大长度 */
  private Integer passwordMaxlength;

  /** 密码最小长度 */
  private Integer passwordMinlength;


  /** 用户名手机号正则表达式 */
  private String mobilePattern;

  /** 验证码类型 */
  private CaptchaType[] captchaTypes;



  /** 网站名称 */
  private String siteName;

  /** 发件人邮箱 */
  private String smtpFromMail;

  /** SMTP服务器地址 */
  private String smtpHost;

  /** SMTP服务器端口 */
  private Integer smtpPort;

  /** SMTP用户名 */
  private String smtpUsername;

  /** SMTP密码 */
  private String smtpPassword;

  private String fileUploadPath;

  /** 允许上传文件扩展名 */
  private String uploadFileExtension;

  /**
   * 允许上传图片扩展名
   */
  private String uploadImageExtension;

  /** 短信验证码过期时间 */
  private Integer smsCodeTimeOut;

  /** 短信服务平台地址 */
  private String ucpaasUrl;

  /** 短信平台AccountId */
  private String ucpaasSid;

  /** 短信平台Token */
  private String ucpaasToken;

  /** 短信平台软件版本 */
  private String ucpaasVersion;

  /** 短信平台APPID */
  private String ucpaasAppId;

  /** 短信平台验证码短信模板 */
  private String ucpaasTemplate;

  /** 短信平台商家账户短信模板 */
  private String ucpaasAccountTemplate;

  /** 短信平台购买服务短信模板 */
  private String ucpaasServiceTemplate;
  /**
   * 短信平台审核失败短信模板ID
   */
  private String applyFailureTemplate;


  /** 短信平台语音回拨号码 */
  private String ucpaasCallDisplay;


  private Long imageMaxSize;
  /**
   * 消息推送
   */
  private String msgPushUrl;


  /**
   * 下载时apk的名字
   */
  private String apkName;



  // 微信的配置参数
  /**
   * 秘钥
   */
  private String wechatKey;

  /**
   * 微信分配的公众账号ID（企业号corpid即为此appId）
   */
  private String wechatAppid;

  /**
   * 商户ID
   */
  private String wechatMchId;

  /**
   * 微信申请退款接口
   */
  private String wechatRefundUrl;


  // 支付宝的配置参数
  /**
   * 支付宝服务URL
   */
  private String alipayServerUrl;
  /**
   * 支付宝appId
   */
  private String alipayAppid;

  /**
   * 支付宝的公钥，无需修改该值
   */
  private String alipayPublicKey;

  public Integer getPasswordMaxlength() {
    return passwordMaxlength;
  }

  public void setPasswordMaxlength(Integer passwordMaxlength) {
    this.passwordMaxlength = passwordMaxlength;
  }

  public Integer getPasswordMinlength() {
    return passwordMinlength;
  }

  public void setPasswordMinlength(Integer passwordMinlength) {
    this.passwordMinlength = passwordMinlength;
  }

  public String getMobilePattern() {
    return mobilePattern;
  }

  public void setMobilePattern(String mobilePattern) {
    this.mobilePattern = mobilePattern;
  }

  public CaptchaType[] getCaptchaTypes() {
    return captchaTypes;
  }

  public void setCaptchaTypes(CaptchaType[] captchaTypes) {
    this.captchaTypes = captchaTypes;
  }

  public String getSiteName() {
    return siteName;
  }

  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }

  public String getSmtpFromMail() {
    return smtpFromMail;
  }

  public void setSmtpFromMail(String smtpFromMail) {
    this.smtpFromMail = smtpFromMail;
  }

  public String getSmtpHost() {
    return smtpHost;
  }

  public void setSmtpHost(String smtpHost) {
    this.smtpHost = smtpHost;
  }

  public Integer getSmtpPort() {
    return smtpPort;
  }

  public void setSmtpPort(Integer smtpPort) {
    this.smtpPort = smtpPort;
  }

  public String getSmtpUsername() {
    return smtpUsername;
  }

  public void setSmtpUsername(String smtpUsername) {
    this.smtpUsername = smtpUsername;
  }

  public String getSmtpPassword() {
    return smtpPassword;
  }

  public void setSmtpPassword(String smtpPassword) {
    this.smtpPassword = smtpPassword;
  }

  public String getFileUploadPath() {
    return fileUploadPath;
  }

  public void setFileUploadPath(String fileUploadPath) {
    this.fileUploadPath = fileUploadPath;
  }

  /**
   * 获取允许上传文件扩展名
   * 
   * @return 允许上传文件扩展名
   */
  @Length(max = 200)
  public String getUploadFileExtension() {
    return uploadFileExtension;
  }

  /**
   * 设置允许上传文件扩展名
   * 
   * @param uploadFileExtension 允许上传文件扩展名
   */
  public void setUploadFileExtension(String uploadFileExtension) {
    if (uploadFileExtension != null) {
      uploadFileExtension =
          uploadFileExtension.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "")
              .toLowerCase();
    }
    this.uploadFileExtension = uploadFileExtension;
  }

  /**
   * 获取允许上传文件扩展名
   * 
   * @return 允许上传文件扩展名
   */
  public String[] getUploadFileExtensions() {
    return StringUtils.split(uploadFileExtension, SEPARATOR);
  }

  public void setUploadImageExtension(String uploadImageExtension) {
    if (uploadImageExtension != null) {
      uploadImageExtension =
          uploadImageExtension.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "")
              .toLowerCase();
    }
    this.uploadImageExtension = uploadImageExtension;
  }

  public String getUploadImageExtension() {
    return uploadImageExtension;
  }

  public String[] getUploadImageExtensions() {
    return StringUtils.split(uploadImageExtension, SEPARATOR);
  }

  public Integer getSmsCodeTimeOut() {
    return smsCodeTimeOut;
  }

  public void setSmsCodeTimeOut(Integer smsCodeTimeOut) {
    this.smsCodeTimeOut = smsCodeTimeOut;
  }

  public String getUcpaasUrl() {
    return ucpaasUrl;
  }

  public void setUcpaasUrl(String ucpaasUrl) {
    this.ucpaasUrl = ucpaasUrl;
  }

  public String getUcpaasSid() {
    return ucpaasSid;
  }

  public void setUcpaasSid(String ucpaasSid) {
    this.ucpaasSid = ucpaasSid;
  }

  public String getUcpaasToken() {
    return ucpaasToken;
  }

  public void setUcpaasToken(String ucpaasToken) {
    this.ucpaasToken = ucpaasToken;
  }

  public String getUcpaasVersion() {
    return ucpaasVersion;
  }

  public void setUcpaasVersion(String ucpaasVersion) {
    this.ucpaasVersion = ucpaasVersion;
  }

  public String getUcpaasAppId() {
    return ucpaasAppId;
  }

  public void setUcpaasAppId(String ucpaasAppId) {
    this.ucpaasAppId = ucpaasAppId;
  }

  public String getUcpaasTemplate() {
    return ucpaasTemplate;
  }

  public void setUcpaasTemplate(String ucpaasTemplate) {
    this.ucpaasTemplate = ucpaasTemplate;
  }

  public String getUcpaasServiceTemplate() {
    return ucpaasServiceTemplate;
  }

  public void setUcpaasServiceTemplate(String ucpaasServiceTemplate) {
    this.ucpaasServiceTemplate = ucpaasServiceTemplate;
  }

  public String getUcpaasCallDisplay() {
    return ucpaasCallDisplay;
  }

  public void setUcpaasCallDisplay(String ucpaasCallDisplay) {
    this.ucpaasCallDisplay = ucpaasCallDisplay;
  }

  public String getUcpaasAccountTemplate() {
    return ucpaasAccountTemplate;
  }

  public void setUcpaasAccountTemplate(String ucpaasAccountTemplate) {
    this.ucpaasAccountTemplate = ucpaasAccountTemplate;
  }

  public Long getImageMaxSize() {
    return imageMaxSize;
  }

  public void setImageMaxSize(Long imageMaxSize) {
    this.imageMaxSize = imageMaxSize;
  }

  public String getMsgPushUrl() {
    return msgPushUrl;
  }

  public void setMsgPushUrl(String msgPushUrl) {
    this.msgPushUrl = msgPushUrl;
  }

  public String getApplyFailureTemplate() {
    return applyFailureTemplate;
  }

  public void setApplyFailureTemplate(String applyFailureTemplate) {
    this.applyFailureTemplate = applyFailureTemplate;
  }

  public String getApkName() {
    return apkName;
  }

  public void setApkName(String apkName) {
    this.apkName = apkName;
  }

  public String getWechatKey() {
    return wechatKey;
  }

  public void setWechatKey(String wechatKey) {
    this.wechatKey = wechatKey;
  }

  public String getWechatAppid() {
    return wechatAppid;
  }

  public void setWechatAppid(String wechatAppid) {
    this.wechatAppid = wechatAppid;
  }

  public String getWechatMchId() {
    return wechatMchId;
  }

  public void setWechatMchId(String wechatMchId) {
    this.wechatMchId = wechatMchId;
  }

  public String getWechatRefundUrl() {
    return wechatRefundUrl;
  }

  public void setWechatRefundUrl(String wechatRefundUrl) {
    this.wechatRefundUrl = wechatRefundUrl;
  }

  public String getAlipayServerUrl() {
    return alipayServerUrl;
  }

  public void setAlipayServerUrl(String alipayServerUrl) {
    this.alipayServerUrl = alipayServerUrl;
  }

  public String getAlipayAppid() {
    return alipayAppid;
  }

  public void setAlipayAppid(String alipayAppid) {
    this.alipayAppid = alipayAppid;
  }

  public String getAlipayPublicKey() {
    return alipayPublicKey;
  }

  public void setAlipayPublicKey(String alipayPublicKey) {
    this.alipayPublicKey = alipayPublicKey;
  }

}

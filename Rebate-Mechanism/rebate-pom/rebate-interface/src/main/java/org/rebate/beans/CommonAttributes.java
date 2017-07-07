package org.rebate.beans;

/**
 * 公共参数
 * 
 */
public final class CommonAttributes {

  /** 日期格式配比 */
  public static final String[] DATE_PATTERNS = new String[] {"yyyy", "yyyy-MM", "yyyyMM",
      "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss",
      "yyyy/MM/dd HH:mm:ss"};

  /** common-config.xml文件路径 */
  public static final String COMMON_CONFIG_XML_PATH = "/common-config.xml";

  /** common-config.properties文件路径 */
  public static final String COMMON_CONFIG_PROPERTIES_PATH = "/common-config.properties";

  public static final String API_TOKEN = "token";

  /** 存放paramMap的 DISTINCT_KEY */
  public static final String DISTINCT_KEY = "distinct";

  /** 存放调用登陆api后返回的token */
  public static final String API_TOKEN_SESSION = "tokenSession";

  public static final String API_USERID = "user_id";

  /** 找回密码时返回的token */
  public static final String PWD_TOKEN = "pwd_token";

  /** 存放调用登陆api后返回的user_id */
  public static final String API_USERID_SESSION = "userIdSession";

  /** 请求成功 */
  public static final String SUCCESS = "0000";// 请求成功

  /** 登录失败 */
  public static final String FAIL_LOGIN = "0001";// 登录失败
  /** 注册失败 */
  public static final String FAIL_REG = "0002";// 注册失败
  /** 短信验证码相关操作失败 */
  public static final String FAIL_SMSTOKEN = "0003";// 短信验证码相关操作失败
  /** Token 失效 */
  public static final String FAIL_TOKEN_TIMEOUT = "0004";// Token 失效
  /** 重置密码失败 */
  public static final String FAIL_RESET_PWD = "0005";// 重置密码失败
  /** 缺失必要参数 */
  public static final String MISSING_REQUIRE_PARAM = "0006";// 缺失必要参数

  public static final String FAIL_USER_WITHDRAW = "0007";// 用户提现
  /** 账号在其它设备上登录 */
  public static final String FAIL_TOKEN_AUTH = "0008";// 账号在其它设备上登录
  /** 会员存在 */
  public static final String FAIL_USER_EXIST = "0009";// 用户已经存在
  //
  // /** OBD设备未绑定车辆 */
  // public static final String FAIL_DEVICE_NOBIND = "0010";// OBD设备不存在
  //
  // /** 车辆车牌号或车架号已存在 */
  // public static final String FAIL_VEHICLE_PLATE_EXIST = "0011"; // 车辆车牌号或车架号已存在
  //
  // /** 手机号已存在 */
  // public static final String FAIL_MOBILENUM_EXIST = "0012"; // 手机号已存在
  // /** 手机号格式错误 */
  // public static final String FAIL_MOBILENUM_BAD_FORMAT = "0013"; // 手机号格式错误
  // /** 操作失败 */
  // public static final String SEND_EMAIL_SUCCESS = "0014"; // 邮件发送成功
  // /** 操作失败 */
  // public static final String SEND_EMAIL_FAILED = "0015"; // 邮件发送失败
  // /** 邮箱格式错误 */
  // public static final String FAIL_EMAIL_BAD_FORMAT = "0016"; // 手机号格式错误
  //
  // /** 优惠券已领完 */
  // public static final String FAIL_COUPON_NO_REMAIN = "0017"; // 优惠券已领完
  //
  // /** 车辆已经绑定租户 */
  // public static final String FAIL_VEHICLE_BIND_TENANT = "0018";// 车辆已经绑定租户
  //
  // /** 无默认车辆 */
  // public static final String FAIL_DEFAULT_VEHICLE = "0019";// 无默认车辆
  // /** 购买OBD设备失败 */
  // public static final String FAIL_PURCHASE_DEVICE = "0020";// 购买OBD设备失败
  //
  // /** 车辆删除失败 */
  // public static final String FAIL_VEHICLE_DELETE = "0021";// 车辆删除失败
  //
  // /** 重复消息 */
  // public static final String FAIL_DEVICE_MSG_DUPLICATE = "0022";// 重复消息,不推送
  //
  /** 操作失败 */
  public static final String FAIL_COMMON = "1000"; // 操作失败

  public static final String USER_INVALID = "3000";// 用户非法（禁用）

  public static final int PAGE_SIZE = 10;//默认每页10条数据
  /**
   * 不可实例化
   */
  private CommonAttributes() {}

}

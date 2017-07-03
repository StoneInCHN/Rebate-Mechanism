package org.rebate.entity.commonenum;

/**
 * 公共枚举数据
 * 
 * @author shijun
 *
 */
public class CommonEnum {

  /**
   * 通用状态
   */
  public enum CommonStatus {

    /** 可用 */
    ACITVE,
    /** 不可用 */
    INACTIVE
  }

  /**
   * 帐号状态
   */
  public enum AdminStatus {

    /** 帐号正常 */
    actived,

    /** 帐号锁定 */
    locked
  }

  /**
   * 帐号状态
   */
  public enum AccountStatus {

    /** 帐号正常 */
    ACTIVED,

    /** 帐号禁用 */
    LOCKED,

    /** 帐号删除 */
    DELETE
  }

  /**
   * 性别
   */
  public enum Gender {

    /** 男 */
    MALE,

    /** 女 */
    FEMALE
  }

  /**
   * 特色服务
   */
  public enum FeaturedService {

    /** 全部 */
    ALL,
    /** WIFI */
    WIFI,
    /** 免费停车 */
    FREE_PARKING
  }

  /**
   * 短信验证码类型
   * 
   * @author sujinxuan
   *
   */
  public enum SmsCodeType {
    /** 注册 */
    REG,
    /** 登录 */
    LOGIN,
    /** 重置密码 */
    RESETPWD,
    /** 修改登录密码 */
    UPDATELOGINPWD,
    /** 修改支付密码 */
    UPDATEPAYPWD,
    /** 银行预留手机号验证 */
    RESERVEDMOBILE
  }

  /**
   * 审核状态
   * 
   *
   */
  public enum ApplyStatus {
    /** 待审核 */
    AUDIT_WAITING,

    /** 审核通过 */
    AUDIT_PASSED,

    /** 审核退回 */
    AUDIT_FAILED

  }

  /**
   * 代理级别
   * 
   *
   */
  public enum AgencyLevel {
    /** 省 */
    PROVINCE,
    /** 市 */
    CITY,
    /** 区,县 */
    COUNTY

  }

  /**
   * 数据字典配置项名称
   * 
   *
   */
  public enum SystemConfigKey {
    /** 支付方式 0 */
    PAYMENTTYPE,
    /** 商户返利积分倍数 1 */
    REBATESCORE_SELLER,
    /** 用户返利积分倍数 2 */
    REBATESCORE_USER,
    /** 收益后乐分比例 3 */
    LESCORE_PERCENTAGE,
    /** 每个乐心分红上限 4 */
    BONUS_MAXIMUM,
    /** 单位消费值（消费UNIT_CONSUME元赠送**积分）5 */
    UNIT_CONSUME,
    /** 乐心算法分母 6 */
    MIND_DIVIDE,
    /** 消费用户的直接推荐人收益百分比 7 */
    RECOMMEND_DIRECT_USER,
    /** 消费用户的间接推荐人收益百分比 8 */
    RECOMMEND_INDIRECT_USER,
    /** 业务员发展商家提成占让利百分比 9 */
    RECOMMEND_SELLER,
    /** 激励乐分提现最低金额限制 10 */
    WITHDRAW_MINIMUM_LIMIT,
    /** 每日分红总额占平台每日让利金额的比例 11 */
    TOTAL_BONUS_PERCENTAGE,
    /** 推荐用户提成的最大推荐层级限制 12 */
    RECOMMEND_LEVEL_LIMIT,
    /** 鼓励金占让利金额百分比 13 */
    ENCOURAGE_CONSUME,
    // /** 商家积分返利订单百分比参数 */
    // REBATESCORE_SELLER_ORDER_PERCENTAGE,
    /** 平台获利占总让利百分比 14 */
    PLATFORM_INCOME_PERCENTAGE,
    /** 创业基金占总让利百分比 15 */
    VENTURE_FUND_PERCENTAGE,
    /** 代理商提成占总让利百分比 16 */
    AGENT_COMMISSION_PERCENTAGE,
    /** 分享佣金占总让利百分比 17 */
    USER_RECOMMEND_COMMISSION_PERCENTAGE,
    /** 提现手续费占提现金额的百分比 18 */
    TRANSACTION_FEE_PERCENTAGE,
    /** 每笔提现固定手续费 19 */
    TRANSACTION_FEE_PERTIME,
    /** 乐心分红value值 20 */
    MIND_VALUE
  }

  /**
   * 设置配置项名称
   * 
   *
   */
  public enum SettingConfigKey {
    /** 软件许可协议 0 */
    LICENSE_AGREEMENT,
    /** 客户电话 1 */
    CUSTOMER_PHONE,
    /** 关于 2 */
    ABOUT_US,
    /** 会员提现规则 3 */
    WITHDRAW_RULE_ENDUSER,
    /** 商家提现规则 4 */
    WITHDRAW_RULE_SELLER,
    /** 代理商提现规则 5 */
    WITHDRAW_RULE_AGENT,
    /** 安卓下载地址 6 */
    ANDROID_DOWNLOAD_URL,
    /** IOS下载地址 7 */
    IOS_DOWNLOAD_URL,
    /** 店铺货款说明 8 */
    SELLER_PAYMENT_DESC,
    /** 实名认证说明 9 */
    USER_AUTH_DESC,
    /** 银行卡持卡人说明 10 */
    BANKCARD_OWNER_DESC,
    /** 银行卡手机号说明 11 */
    BANKCARD_MOBILE_DESC,
    /** 银行卡服务协议 12 */
    BANKCARD_SERVICE_AGREEMENT,
    /** 乐豆抵扣说明 13 */
    LEBEAN_PAY_DESC,
    /** 消费人数统计倍数(用于手机端显示) 14 */
    CONSUME_PEOPLE_MULTIPLE,
    /** 乐豆支付是否产生收益 15 */
    BEAN_INCOME_SWITCH
  }

  /**
   * 乐分类型
   */
  public enum LeScoreType {
    /** 乐分消费 */
    CONSUME,
    // /** 用户消费鼓励金收益 */
    // ENCOURAGE,
    /** 乐心（积分）产生的分红 */
    BONUS,
    /** 推荐好友消费返利 */
    RECOMMEND_USER,
    /** 推荐店铺收益返利 */
    RECOMMEND_SELLER,
    /** 代理商提成 */
    AGENT,
    /** 提现 */
    WITHDRAW,
    /** 乐分退回 */
    REFUND
    
  }

  /**
   * app platform
   * 
   */
  public enum AppPlatform {
    /** android */
    ANDROID,
    /** IOS */
    IOS
  }

  /**
   * 乐豆变化类型
   * 
   */
  public enum LeBeanChangeType {
    /** 乐心分红赠送乐豆 */
    BONUS,
    /** 推荐好友消费送乐豆 */
    RECOMMEND_USER,
    /** 推荐店铺收益送乐豆 */
    RECOMMEND_SELLER,
    /** 消费 */
    CONSUME,
    /** 用户消费鼓励金收益 */
    ENCOURAGE,
  }

  public enum ImageType {
    /** 头像 */
    PHOTO,
    /** 店铺环境 */
    STORE_ENV,
    /** 店铺营业执照 */
    STORE_LICENSE,
    /** 店铺列表展示图片 */
    STORE_SIGN,
    /** 商户承诺书图片 */
    STORE_COMMITMENT,
    /** 订单评论图片 */
    ORDER_EVALUATE,
    /** 身份证照片 */
    AUTH_IDCARD
  }

  public enum FileType {

    /** 图片 */
    image,
    /** 文件 */
    file
  }

  public enum OrderStatus {
    /** 未支付 */
    UNPAID,
    /** 已支付，待评价 */
    PAID,
    /** 评价后，已完成 */
    FINISHED
  }

  /**
   * 手机接口查询排序类型
   * 
   */
  public enum SortType {
    /**
     * 距离由近及远
     */
    DISTANCEASC,
    /**
     * 好评分由高到低
     */
    SCOREDESC,
    /**
     * 收藏由高到低
     */
    COLLECTDESC,
    /** 默认排序（时间先后顺序） */
    DEFAULT
  }
  /**
   * 根据回复状态查看订单
   * 
   * @author huyong
   *
   */
  public enum RequestReplyStatus {
    /**
     * 全部
     */
    ALL,
    /**
     * 已回复
     */
    REPLAY_STATUS,
    /**
     * 未回复
     */
    NO_REPLAY_STATUS
  }
  /**
   * 货款结算（提现）状态
   *
   */
  public enum ClearingStatus {
    /** 处理中 **/
    PROCESSING,
    /** 处理成功 **/
    SUCCESS,
    /** 处理失败 **/
    FAILED

  }
  /**
   * 系统参数配置
   *
   */
  public enum ParamConfigKey {
    /** 交易结果查询 延迟查询时间 0 */
    ALLINPAY_QUERY_DELAY,
    /** 交易结果查询 间隔时间(每五分钟) 1 */
    ALLINPAY_QUERY_PERIOD
  }
  /**
   * 图片大小
   *
   */
  public enum ImageSize {
	  /** 小图*/
	  SMALL,
	  /** 中图*/
	  MIDDLE,
	  /** 大图*/
	  BIG
  }
  /**
   * 二维码类型
   *
   */
  public enum QrCodeType {
	  /** 推广二维码*/
	  SHARE,
	  /** 支付二维码*/
	  PAID
  }
}

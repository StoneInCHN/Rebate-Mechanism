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
    UPDATEPAYPWD
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
    /** 县 */
    COUNTY,
    /** 镇 */
    TOWN

  }

  /**
   * 配置项名称
   * 
   *
   */
  public enum SystemConfigKey {
    /** 支付方式 0 */
    PAYMENTTYPE,
    /** 商户返利积分参数 1 */
    REBATESCORE_SELLER,
    /** 用户返利积分参数2 */
    REBATESCORE_USER,
    /** 收益后乐分乐豆比例 3 */
    LESCORE_PERCENTAGE,
    /** 每个乐心分红的阈值 4 */
    BONUS_MAXIMUM,
    /** 单位消费值（消费UNIT_CONSUME元赠送**积分）5 */
    UNIT_CONSUME,
    /** 乐心算法分母 6 */
    MIND_DIVIDE,
    /** 消费用户的直接推荐人收益百分比 7 */
    RECOMMEND_DIRECT_USER,
    /** 消费用户的间接推荐人收益百分比 8 */
    RECOMMEND_INDIRECT_USER,
    /** 消费商户的推荐人收益百分比 9 */
    RECOMMEND_SELLER
  }

  /**
   * 乐分类型
   */
  public enum LeScoreType {
    /** 消费直接返商户的收益 */
    CONSUME_SELLER,
    /** 乐心（积分）产生的分红 */
    BONUS,
    /** 推荐好友消费返利 */
    RECOMMEND_USER,
    /** 推荐店铺收益返利 */
    RECOMMEND_SELLER,
    /** 代理商提成 */
    AGENT,
    /** 提现 */
    WITHDRAW
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
    /** 消费 */
    CONSUME
  }

  public enum ImageType {
    /** 头像 */
    PHOTO,
    /** 店铺环境 */
    STORE_ENV,
    /** 店铺营业执照 */
    STORE_LICENSE,
    /** 店铺列表展示图片 */
    STORE_SIGN
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
    /** 已完成 */
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
}

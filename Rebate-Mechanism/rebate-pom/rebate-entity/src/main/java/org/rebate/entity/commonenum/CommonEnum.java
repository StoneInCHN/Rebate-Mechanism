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
    RESETPWD
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
    /** 支付方式 */
    PAYMENTTYPE,
    /** 商户返利积分参数 */
    REBATESCORE_SELLER,
    /** 用户返利积分参数 */
    REBATESCORE_USER,
    /** 收益后乐分乐豆比例 */
    LESCORE_PERCENTAGE,
    /** 每个乐心分红的阈值 */
    BONUS_MAXIMUM,
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
    AGENT
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

  public enum FileType {

    /** 图片 */
    image,
    /** 文件 */
    file
  }
}

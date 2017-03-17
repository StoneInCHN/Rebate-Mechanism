package org.rebate.entity.commonenum;

/**
 * 公共枚举数据
 * 
 * @author shijun
 *
 */
public class CommonEnum {
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
   * 支付方式
   */
  public enum PaymentType {

    /** 翼支付 */
    TELECOM,
    /** 微信支付 */
    WECHAT,
    /** 支付宝支付 */
    ALIPAY,
    /** 代金券支付 */
    COUPON
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

}

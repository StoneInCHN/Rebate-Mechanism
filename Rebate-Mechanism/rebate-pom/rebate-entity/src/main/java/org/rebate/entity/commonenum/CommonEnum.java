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

  // /**
  // * 支付方式
  // */
  // public enum PaymentType {
  // /** 翼支付 */
  // TELECOM,
  // /** 微信支付 */
  // WECHAT,
  // /** 支付宝支付 */
  // ALIPAY,
  // /** 乐分支付 */
  // LESCORE
  // }

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
    PAYMENTTYPE

  }

  /**
   * 返利类型
   */
  public enum RebateType {
    /** 消费返用户 */
    CONSUME_USER,
    /** 消费返商户 */
    CONSUME_SELLER,
    /** 推荐返利 */
    RECOMMEND,
    /** 代理商提成 */
    AGENT
  }
  public enum FileType {

    /** 图片 */
    image,
    /** 文件 */
    file
  }
}

package org.rebate.entity;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.rebate.entity.base.BaseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * APP首页上面广告
 * 
 * @author shijun
 *
 */
@Entity
@Table(name = "rm_top_banner")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_top_banner_sequence")
public class TopBanner extends BaseEntity{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * banner名称
   */
  private String bannerName;
  
  /**
   * banner URL
   */
  private String bannerUrl;
  
  /**
   * 是否生效
   */
  private Boolean isActive;
  
  /**
   * banner 图片内容
   */
  private MultipartFile bannerPicture;

  public String getBannerName() {
    return bannerName;
  }

  public void setBannerName(String bannerName) {
    this.bannerName = bannerName;
  }

  public String getBannerUrl() {
    return bannerUrl;
  }

  public void setBannerUrl(String bannerUrl) {
    this.bannerUrl = bannerUrl;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public MultipartFile getBannerPicture() {
    return bannerPicture;
  }

  public void setBannerPicture(MultipartFile bannerPicture) {
    this.bannerPicture = bannerPicture;
  }
}

package org.rebate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.rebate.entity.base.BaseEntity;
import org.rebate.entity.commonenum.CommonEnum.AppPlatform;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "rm_apk_version")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_apk_version_sequence")
public class ApkVersion extends BaseEntity {

  /**
   * 
   */
  private static final long serialVersionUID = 3310941691877457267L;

  /**
   * 版本号
   */
  private String versionName;

  /**
   * 版本序列号(用于比较是否是新版本)
   */
  private Integer versionCode = 0;
  /**
   */
  private String apkPath;

  /**
   * apk文件
   */
  private MultipartFile file;


  /**
   * 版本更新内容
   */
  private String updateContent;

  /**
   * 是否强制更新
   */
  private Boolean isForced;

  /**
   * app平台
   */
  private AppPlatform appPlatform;


  public AppPlatform getAppPlatform() {
    return appPlatform;
  }

  public void setAppPlatform(AppPlatform appPlatform) {
    this.appPlatform = appPlatform;
  }

  public Boolean getIsForced() {
    return isForced;
  }

  public void setIsForced(Boolean isForced) {
    this.isForced = isForced;
  }

  @Column(length = 20)
  public String getVersionName() {
    return versionName;
  }

  public void setVersionName(String versionName) {
    this.versionName = versionName;
  }

  @Column(length = 20)
  public Integer getVersionCode() {
    return versionCode;
  }

  public void setVersionCode(Integer versionCode) {
    this.versionCode = versionCode;
  }

  @Column(length = 200)
  public String getApkPath() {
    return apkPath;
  }

  public void setApkPath(String apkPath) {
    this.apkPath = apkPath;
  }


  @Column(length = 500)
  public String getUpdateContent() {
    return updateContent;
  }

  public void setUpdateContent(String updateContent) {
    this.updateContent = updateContent;
  }

  @Transient
  public MultipartFile getFile() {
    return file;
  }

  public void setFile(MultipartFile file) {
    this.file = file;
  }

}

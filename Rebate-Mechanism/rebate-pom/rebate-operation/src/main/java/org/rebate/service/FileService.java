package org.rebate.service;

import org.rebate.beans.Setting.ImageType;
import org.rebate.entity.commonenum.CommonEnum.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
  /**
   * 上传头像图片
   * 
   * @param multipartFile
   * @return
   */
  public String saveImage(MultipartFile[] multipartFile);

  public String saveImage(MultipartFile multipartFile, ImageType imageType);

  /**
   * 
   * @param multiFile
   * @param imageType
   * @param hasFullPath 是否添加全路径
   * @return
   */
  public String saveImage(MultipartFile multiFile, ImageType imageType,Boolean hasFullPath);
  
  /**
   * 文件验证
   * 
   * @param fileType
   *            文件类型
   * @param multipartFile
   *            上传文件
   * @return 文件验证是否通过
   */
  boolean isValid(FileType fileType, MultipartFile multipartFile);
  
  /**
   * 文件上传
   * 
   * @param fileType 文件类型
   * @param multipartFile 上传文件
   * @param async 是否异步
   * @return 访问URL
   */
  String upload(FileType fileType, MultipartFile multipartFile, boolean async);

  /**
   * 文件上传(异步)
   * 
   * @param fileType 文件类型
   * @param multipartFile 上传文件
   * @return 访问URL
   */
  String upload(FileType fileType, MultipartFile multipartFile);
  /**
   * 上传APK文件
   * @param multipartFile
   * @return
   */
  String uploadApk(MultipartFile multipartFile);

}

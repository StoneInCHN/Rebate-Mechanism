package org.rebate.service;

import org.rebate.entity.EndUser;
import org.rebate.entity.commonenum.CommonEnum.ImageType;
import org.rebate.framework.service.BaseService;
import org.springframework.web.multipart.MultipartFile;


/**
 * 上传图片
 * 
 * @author sujinxuan
 *
 */
public interface FileService extends BaseService<EndUser, Long> {
  /**
   * 批量上传图片
   * 
   * @param multipartFile
   * @return
   */
  public String saveImage(MultipartFile[] multipartFile);

  /**
   * 按类型上传图片
   * 
   * @param multipartFile
   * @param imageType
   * @return
   */
  public String saveImage(MultipartFile multipartFile, ImageType imageType);


}

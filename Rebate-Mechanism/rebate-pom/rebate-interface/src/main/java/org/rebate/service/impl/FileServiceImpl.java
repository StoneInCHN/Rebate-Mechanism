package org.rebate.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.rebate.entity.commonenum.CommonEnum.ImageType;
import org.rebate.service.FileService;
import org.rebate.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("fileServiceImpl")
public class FileServiceImpl implements FileService {

  private static final String DEST_EXTENSION = "jpg";

  @Value("${ImageUploadPath}")
  private String uploadPath;
  @Value("${ProjectUploadPath}")
  private String projectUploadPath;
  @Value("${ImageWidth}")
  private Integer imageWidth;
  @Value("${ImageHeight}")
  private Integer imageHeight;


  @Override
  public String saveImage(MultipartFile[] multipartFile) {
    String webPath = null;
    String projectPath = projectUploadPath;
    if (multipartFile == null || multipartFile.length == 0) {
      return null;
    }
    try {
      for (MultipartFile multiFile : multipartFile) {
        String uuid = UUID.randomUUID().toString();
        String sourcePath =
            uploadPath + File.separator + "src_" + uuid + "."
                + FilenameUtils.getExtension(multiFile.getOriginalFilename());
        webPath =
            projectPath + File.separator + "src_" + uuid + "."
                + FilenameUtils.getExtension(multiFile.getOriginalFilename());
        // String storePath = uploadPath + File.separator + uuid + "." + DEST_EXTENSION;;

        File tempFile =
            new File(System.getProperty("java.io.tmpdir") + File.separator + "upload_"
                + UUID.randomUUID() + ".tmp");
        if (!tempFile.getParentFile().exists()) {
          tempFile.getParentFile().mkdirs();
        }

        multiFile.transferTo(tempFile);
        proccessImage(tempFile, sourcePath);
      }
    } catch (IllegalStateException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return webPath;
  }

  /**
   * 处理并保存图片
   * 
   * @param tempFile
   * @param sourcePath
   * @param resizedPath
   * @param width
   * @param height
   * @param moveSource
   */
  private void proccessImage(File tempFile, String sourcePath, String resizedPath, Integer width,
      Integer height, boolean moveSource) {
    String tempPath = System.getProperty("java.io.tmpdir");
    File resizedFile =
        new File(tempPath + File.pathSeparator + "upload_" + UUID.randomUUID() + "."
            + DEST_EXTENSION);
    ImageUtils.zoom(tempFile, resizedFile, width, height);

    File destFile = new File(resizedPath);
    try {
      if (moveSource) {
        File destSrcFile = new File(sourcePath);
        FileUtils.moveFile(tempFile, destSrcFile);
      }
      FileUtils.moveFile(resizedFile, destFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 直接保存手机端图片，不处理
   * 
   * @param tempFile
   * @param sourcePath
   * @param resizedPath
   */
  private void proccessImage(File tempFile, String sourcePath) {
    try {
      File destSrcFile = new File(sourcePath);
      FileUtils.moveFile(tempFile, destSrcFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String saveImage(MultipartFile multiFile, ImageType imageType) {
    String webPath = null;
    String imgUploadPath = "";
    String projectPath = "";
    try {

      if (multiFile == null) {
        return null;
      }
      String uuid = UUID.randomUUID().toString();

      if (imageType == ImageType.STORE_LICENSE) {
        imgUploadPath = uploadPath + File.separator + "store" + File.separator + "license";
        projectPath = projectUploadPath + File.separator + "store" + File.separator + "license";
      } else if (imageType == ImageType.STORE_ENV) {
        imgUploadPath = uploadPath + File.separator + "store" + File.separator + "env";
        projectPath = projectUploadPath + File.separator + "store" + File.separator + "env";
      } else if (imageType == ImageType.STORE_SIGN) {
        imgUploadPath = uploadPath + File.separator + "store" + File.separator + "sign";
        projectPath = projectUploadPath + File.separator + "store" + File.separator + "sign";
      } else if (imageType == ImageType.PHOTO) {
        imgUploadPath = uploadPath + File.separator + "profile";
        projectPath = projectUploadPath + File.separator + "profile";
      }
      // if (imageType == ImageType.AUDIO) {
      // imgUploadPath = uploadPath+ File.separator+"audio";
      // projectPath = projectUploadPath+File.separator+"audio";
      // }
      // if (imageType == ImageType.SUGGEST) {
      // imgUploadPath = uploadPath+ File.separator+"suggest";
      // projectPath = projectUploadPath+File.separator+"suggest";
      // }
      String sourcePath =
          imgUploadPath + File.separator + "src_" + uuid + "."
              + FilenameUtils.getExtension(multiFile.getOriginalFilename());
      webPath =
          projectPath + File.separator + "src_" + uuid + "."
              + FilenameUtils.getExtension(multiFile.getOriginalFilename());
      // String storePath = imgUploadPath + File.separator + uuid + "." + DEST_EXTENSION;;

      File tempFile =
          new File(System.getProperty("java.io.tmpdir") + File.separator + "upload_"
              + UUID.randomUUID() + ".tmp");
      if (!tempFile.getParentFile().exists()) {
        tempFile.getParentFile().mkdirs();
      }
      multiFile.transferTo(tempFile);
      proccessImage(tempFile, sourcePath);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return webPath;
  }

}

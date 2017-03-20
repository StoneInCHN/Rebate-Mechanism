package org.rebate.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.rebate.beans.Setting;
import org.rebate.beans.Setting.ImageType;
import org.rebate.entity.commonenum.CommonEnum.FileType;
import org.rebate.service.FileService;
import org.rebate.utils.ImageUtils;
import org.rebate.utils.SettingUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.icu.text.SimpleDateFormat;

@Service("fileServiceImpl")
public class FileServiceImpl implements FileService {

  private static final String DEST_EXTENSION = "jpg";

  @Resource(name = "taskExecutor")
  private TaskExecutor taskExecutor;


  @Value("${ProjectUploadPath}")
  private String projectUploadPath;
  @Value("${uploadPath}")
  private String uploadPath;
  @Value("${ImageMaxSize}")
  private Integer ImageMaxSize;
  @Value("${fileMaxSize}")
  private Integer fileMaxSize;

  @Value("${ListImageWidth}")
  private Integer listImageWidth;
  @Value("${ListImageHeight}")
  private Integer listImageHeight;
  @Value("${AutoServiceImageHeight}")
  private Integer autoServiceImageHeight;


  @Override
  public String saveImage(MultipartFile[] multipartFile) {
    String webPath = null;
    if (multipartFile == null || multipartFile.length == 0) {
      return null;
    }
    File tempFile =
        new File(System.getProperty("java.io.tmpdir") + File.separator + "upload_"
            + UUID.randomUUID() + ".tmp");
    try {
      for (MultipartFile multiFile : multipartFile) {
        if (multiFile.getSize() > ImageMaxSize) {
          continue;
        }
        String uuid = UUID.randomUUID().toString();
        String sourcePath =
            uploadPath + File.separator + "src_" + uuid + "."
                + FilenameUtils.getExtension(multiFile.getOriginalFilename());
        webPath =
            File.separator + "src_" + uuid + "."
                + FilenameUtils.getExtension(multiFile.getOriginalFilename());
        String storePath = uploadPath + File.separator + uuid + "." + DEST_EXTENSION;;
        
        if (!tempFile.getParentFile().exists()) {
          tempFile.getParentFile().mkdirs();
        }

        multiFile.transferTo(tempFile);
        proccessImage(tempFile, sourcePath, storePath, listImageWidth, listImageHeight, true);
      }
    } catch (IllegalStateException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      FileUtils.deleteQuietly(tempFile);
    }

    return webPath;
  }

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

  @Override
  public String saveImage(MultipartFile multiFile, ImageType imageType) {
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
    String date = sdFormat.format(new Date());
    String webPath = null;
    String imgUploadPath = "";
    String subPath = "";

    if (multiFile == null || multiFile.getSize() > ImageMaxSize) {
      return null;
    }
    String uuid = UUID.randomUUID().toString();
    if (imageType == ImageType.LICENSE) {
      subPath = File.separator + "license";
    } else if (imageType == ImageType.STOREPICTURE) {
      subPath = File.separator + "storePicture";
    } else if (imageType == ImageType.ADVERTISEMENT) {
      subPath = File.separator + "advertisement";
    } else if (imageType == ImageType.VEHICLEICON) {
      subPath = File.separator + "vehicleIcon";
    } else if (imageType == ImageType.NEWS) {
      subPath = File.separator + "news";
    }else if (imageType == ImageType.NEWSGCATEGORY) {
      subPath = File.separator + "newsCagetory";
    }else if (imageType == ImageType.Coupon) {
      subPath = File.separator + "coupon";
    }else if (imageType == ImageType.BrandLogo) {
      subPath = File.separator + "logo";
    }

    imgUploadPath = uploadPath + subPath;

    String sourcePath =
        imgUploadPath + File.separator + date + File.separator + "src_" + uuid + "."
            + FilenameUtils.getExtension(multiFile.getOriginalFilename());
    if (imageType == ImageType.NEWS) {
      webPath =
          projectUploadPath + subPath + File.separator + date + File.separator + "src_" + uuid
              + "." + FilenameUtils.getExtension(multiFile.getOriginalFilename());
    } else {
      webPath =
          File.separator + "upload" + subPath + File.separator + date + File.separator + "src_"
              + uuid + "." + FilenameUtils.getExtension(multiFile.getOriginalFilename());
    }

    String storePath =
        imgUploadPath + File.separator + date + File.separator + uuid + "." + DEST_EXTENSION;;

    File tempFile =
        new File(System.getProperty("java.io.tmpdir") + File.separator + "upload_"
            + UUID.randomUUID() + ".tmp");
    try {
      if (!tempFile.getParentFile().exists()) {
        tempFile.getParentFile().mkdirs();
      }
      multiFile.transferTo(tempFile);
      proccessImage(tempFile, sourcePath, storePath, listImageWidth, listImageHeight, true);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      FileUtils.deleteQuietly(tempFile);
    }
    return webPath;
  }

  @Override
  public String saveImage(MultipartFile multiFile, ImageType imageType,Boolean hasFullPath) {
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
    String date = sdFormat.format(new Date());
    String webPath = null;
    String imgUploadPath = "";
    String subPath = "";

    if (multiFile == null || multiFile.getSize() > ImageMaxSize) {
      return null;
    }
    String uuid = UUID.randomUUID().toString();
    if (imageType == ImageType.LICENSE) {
      subPath = File.separator + "license";
    } else if (imageType == ImageType.STOREPICTURE) {
      subPath = File.separator + "storePicture";
    } else if (imageType == ImageType.ADVERTISEMENT) {
      subPath = File.separator + "advertisement";
    } else if (imageType == ImageType.VEHICLEICON) {
      subPath = File.separator + "vehicleIcon";
    } else if (imageType == ImageType.NEWS) {
      subPath = File.separator + "news";
    }else if (imageType == ImageType.NEWSGCATEGORY) {
      subPath = File.separator + "newsCagetory";
    }else if (imageType == ImageType.Coupon) {
      subPath = File.separator + "coupon";
    }else if (imageType == ImageType.BrandLogo) {
      subPath = File.separator + "logo";
    }

    imgUploadPath = uploadPath + subPath;

    String sourcePath =
        imgUploadPath + File.separator + date + File.separator + "src_" + uuid + "."
            + FilenameUtils.getExtension(multiFile.getOriginalFilename());
    if (hasFullPath) {
      webPath =
          projectUploadPath + subPath + File.separator + date + File.separator + "src_" + uuid
              + "." + FilenameUtils.getExtension(multiFile.getOriginalFilename());
    } else {
      webPath =
          File.separator + "upload" + subPath + File.separator + date + File.separator + "src_"
              + uuid + "." + FilenameUtils.getExtension(multiFile.getOriginalFilename());
    }

    String storePath =
        imgUploadPath + File.separator + date + File.separator + uuid + "." + DEST_EXTENSION;;

    File tempFile =
        new File(System.getProperty("java.io.tmpdir") + File.separator + "upload_"
            + UUID.randomUUID() + ".tmp");
    try {
      if (!tempFile.getParentFile().exists()) {
        tempFile.getParentFile().mkdirs();
      }
      multiFile.transferTo(tempFile);
      proccessImage(tempFile, sourcePath, storePath, listImageWidth, listImageHeight, true);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      FileUtils.deleteQuietly(tempFile);
    }
    return webPath;
  }
  
  public boolean isValid(FileType fileType, MultipartFile multipartFile) {
    if (multipartFile == null) {
      return false;
    }
    Setting setting = SettingUtils.get();
    String[] uploadExtensions = null;
    if (fileType == FileType.file) {
      uploadExtensions = setting.getUploadFileExtensions();
    } else if (fileType == FileType.image) {
      uploadExtensions = setting.getUploadImageExtensions();
    }
    if (!ArrayUtils.isEmpty(uploadExtensions)) {
      return FilenameUtils.isExtension(multipartFile.getOriginalFilename().toLowerCase(),
          uploadExtensions);
    }
    return false;
  }

  public String upload(FileType fileType, MultipartFile multipartFile, boolean async) {
    String webPath = null;
    String fileUploadPath = "";
    String projectPath = "";
    if (multipartFile == null) {
      return null;
    }
    if (fileType == FileType.file) {
      fileUploadPath = uploadPath + File.separator + "apk";
      projectPath = projectUploadPath + File.separator + "apk";
    }
    try {
      String destPath = fileUploadPath + File.separator + multipartFile.getOriginalFilename();
      webPath = projectPath + File.separator + multipartFile.getOriginalFilename();
      File tempFile =
          new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
      if (!tempFile.getParentFile().exists()) {
        tempFile.getParentFile().mkdirs();
      }
      multipartFile.transferTo(tempFile);
      if (async) {
        addTask(destPath, tempFile);
      } else {
        try {
          File destFile = new File(destPath);
          try {
            FileUtils.moveFile(tempFile, destFile);
          } catch (IOException e) {
            e.printStackTrace();
          }
        } finally {
          FileUtils.deleteQuietly(tempFile);
        }
      }
      return webPath;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public String upload(FileType fileType, MultipartFile multipartFile) {
    return upload(fileType, multipartFile, false);
  }

  public String uploadApk(MultipartFile multipartFile) {
    Setting setting = SettingUtils.get();
    String apkName = setting.getApkName();
    String webPath = null;
    String fileUploadPath = uploadPath + File.separator + "apk";;
    String projectPath = projectUploadPath + File.separator + "apk";;
    if (multipartFile == null) {
      return null;
    }
    try {
      String destPath = fileUploadPath + File.separator + multipartFile.getOriginalFilename();
      String downloadApkPath = fileUploadPath + File.separator + apkName;
      webPath = projectPath + File.separator + multipartFile.getOriginalFilename();
      File tempFile =
          new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
      if (!tempFile.getParentFile().exists()) {
        tempFile.getParentFile().mkdirs();
      }
      multipartFile.transferTo(tempFile);
      try {
        File destFile = new File(destPath);
        File downloadApkFile = new File(downloadApkPath);
        try {
          FileUtils.moveFile(tempFile, destFile);
          downloadApkFile.deleteOnExit();
          downloadApkFile = new File(downloadApkPath);
          FileUtils.copyFile(destFile, downloadApkFile);
        } catch (IOException e) {
          e.printStackTrace();
        }
      } finally {
        FileUtils.deleteQuietly(tempFile);
      }
      return webPath;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 添加上传任务
   * 
   * @param path 上传路径
   * @param tempFile 临时文件
   */
  private void addTask(final String path, final File tempFile) {
    taskExecutor.execute(new Runnable() {
      public void run() {
        try {
          File destFile = new File(path);
          try {
            FileUtils.moveFile(tempFile, destFile);
          } catch (IOException e) {
            e.printStackTrace();
          }
        } finally {
          FileUtils.deleteQuietly(tempFile);
        }
      }
    });
  }


}

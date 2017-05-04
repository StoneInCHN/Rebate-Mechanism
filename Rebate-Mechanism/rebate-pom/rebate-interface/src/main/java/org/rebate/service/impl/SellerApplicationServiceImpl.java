package org.rebate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.dao.AreaDao;
import org.rebate.dao.EndUserDao;
import org.rebate.dao.SellerApplicationDao;
import org.rebate.dao.SellerCategoryDao;
import org.rebate.entity.Area;
import org.rebate.entity.EndUser;
import org.rebate.entity.SellerApplication;
import org.rebate.entity.SellerCategory;
import org.rebate.entity.SellerEnvImage;
import org.rebate.entity.commonenum.CommonEnum.ApplyStatus;
import org.rebate.entity.commonenum.CommonEnum.ImageType;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.json.request.SellerRequest;
import org.rebate.service.FileService;
import org.rebate.service.SellerApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service("sellerApplicationServiceImpl")
public class SellerApplicationServiceImpl extends BaseServiceImpl<SellerApplication, Long>
    implements SellerApplicationService {

  @Resource(name = "sellerApplicationDaoImpl")
  private SellerApplicationDao sellerApplicationDao;

  @Resource(name = "areaDaoImpl")
  private AreaDao areaDao;

  @Resource(name = "endUserDaoImpl")
  private EndUserDao endUserDao;

  @Resource(name = "sellerCategoryDaoImpl")
  private SellerCategoryDao sellerCategoryDao;

  @Resource(name = "sellerApplicationDaoImpl")
  public void setBaseDao(SellerApplicationDao sellerApplicationDao) {
    super.setBaseDao(sellerApplicationDao);
  }

  @Resource(name = "fileServiceImpl")
  private FileService fileService;

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public SellerApplication createApplication(SellerRequest req) {
    SellerApplication application = new SellerApplication();
    if (req.getApplyId() != null) {
      application = sellerApplicationDao.find(req.getApplyId());
    }

    application.setSellerName(req.getSellerName());
    application.setAddress(req.getAddress());
    application.setContactCellPhone(req.getContactCellPhone());
    application.setStorePhone(req.getStorePhone());
    application.setLicenseNum(req.getLicenseNum());
    application.setDescription(req.getNote());
    application.setLatitude(new BigDecimal(req.getLatitude()));
    application.setLongitude(new BigDecimal(req.getLongitude()));
    application.setDiscount(new BigDecimal(req.getDiscount()));
    application.setApplyStatus(ApplyStatus.AUDIT_WAITING);

    EndUser endUser = endUserDao.find(req.getUserId());
    application.setEndUser(endUser);

    Area area = areaDao.find(req.getAreaId());
    application.setArea(area);

    SellerCategory sellerCategory = sellerCategoryDao.find(req.getCategoryId());
    application.setSellerCategory(sellerCategory);

    application.setStorePhoto(fileService.saveImage(req.getStorePicture(), ImageType.STORE_SIGN));
    application
        .setLicenseImgUrl(fileService.saveImage(req.getLicenseImg(), ImageType.STORE_LICENSE));

    if (!CollectionUtils.isEmpty(req.getEnvImgs())) {
      List<SellerEnvImage> envImages = new ArrayList<SellerEnvImage>();
      for (MultipartFile file : req.getEnvImgs()) {
        SellerEnvImage envImg = new SellerEnvImage();
        envImg.setSource(fileService.saveImage(file, ImageType.STORE_ENV));
        envImages.add(envImg);
      }
      application.setEnvImages(envImages);
    }


    sellerApplicationDao.merge(application);
    return application;
  }
}

package org.rebate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.rebate.dao.AreaDao;
import org.rebate.dao.SellerDao;
import org.rebate.dao.SystemConfigDao;
import org.rebate.entity.Area;
import org.rebate.entity.EndUser;
import org.rebate.entity.Seller;
import org.rebate.entity.SellerEnvImage;
import org.rebate.entity.commonenum.CommonEnum.ImageType;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.rebate.framework.service.impl.BaseServiceImpl;
import org.rebate.json.request.SellerRequest;
import org.rebate.service.FileService;
import org.rebate.service.SellerService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service("sellerServiceImpl")
public class SellerServiceImpl extends BaseServiceImpl<Seller, Long> implements SellerService {

  @Resource(name = "sellerDaoImpl")
  private SellerDao sellerDao;

  @Resource(name = "fileServiceImpl")
  private FileService fileService;

  @Resource(name = "systemConfigDaoImpl")
  private SystemConfigDao systemConfigDao;

  @Resource(name = "areaDaoImpl")
  private AreaDao areaDao;

  @Resource(name = "jdbcTemplate")
  private JdbcTemplate jdbcTemplate;

  @Resource(name = "sellerDaoImpl")
  public void setBaseDao(SellerDao sellerDao) {
    super.setBaseDao(sellerDao);
  }

  @Override
  public Seller findSellerByUser(Long userId) {
    return sellerDao.findSellerByUser(userId);
  }

  @Override
  public Seller editInfo(SellerRequest req) {
    Seller seller = sellerDao.find(req.getSellerId());
    if (req.getStorePicture() != null) {
      seller.setStorePictureUrl(fileService.saveImage(req.getStorePicture(), ImageType.STORE_SIGN));
    }
    seller.setAvgPrice(new BigDecimal(req.getAvgPrice()));
    seller.setBusinessTime(req.getBusinessTime());
    seller.setName(req.getSellerName());
    seller.setAddress(req.getAddress());
    Area area = areaDao.find(req.getAreaId());
    seller.setArea(area);
    seller.setLatitude(new BigDecimal(req.getLatitude()));
    seller.setLongitude(new BigDecimal(req.getLongitude()));
    seller.setFeaturedService(req.getFeaturedService());
    seller.setDescription(req.getNote());

    if (!CollectionUtils.isEmpty(req.getEnvImgs())) {
      List<SellerEnvImage> envImages = new ArrayList<SellerEnvImage>();
      for (MultipartFile file : req.getEnvImgs()) {
        SellerEnvImage envImg = new SellerEnvImage();
        envImg.setSource(fileService.saveImage(file, ImageType.STORE_ENV));
        envImages.add(envImg);
      }
      seller.setEnvImages(envImages);
    }

    sellerDao.merge(seller);
    return seller;
  }

  @Override
  public Page<Seller> findFavoriteSellers(Pageable pageable, Long userId) {
    return sellerDao.findFavoriteSellers(pageable, userId);
  }

  @Override
  public EndUser userCollectSeller(Long userId, Long sellerId) {
    return sellerDao.userCollectSeller(userId, sellerId);
  }

  @Override
  public Seller findSellerBylicense(String license) {
    return sellerDao.findSellerBylicense(license);
  }

}

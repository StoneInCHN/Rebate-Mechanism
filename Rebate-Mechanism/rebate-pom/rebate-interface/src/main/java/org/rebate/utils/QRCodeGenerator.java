package org.rebate.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.apache.commons.codec.digest.DigestUtils;
import org.rebate.common.log.LogUtil;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeGenerator {

  public QRCodeGenerator() {

  }

  public static byte[] generateQrImage(String uuid) {
    int size = 177;
    byte[] bytes = null;
    try {
      Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap =
          new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
      hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
      QRCodeWriter qrCodeWriter = new QRCodeWriter();
      BitMatrix byteMatrix = qrCodeWriter.encode(uuid, BarcodeFormat.QR_CODE, size, size, hintMap);
      int CrunchifyWidth = byteMatrix.getWidth();
      BufferedImage image =
          new BufferedImage(CrunchifyWidth, CrunchifyWidth, BufferedImage.TYPE_INT_RGB);
      image.createGraphics();

      Graphics2D graphics = (Graphics2D) image.getGraphics();
      graphics.setColor(Color.WHITE);
      graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
      graphics.setColor(Color.BLACK);

      for (int i = 0; i < CrunchifyWidth; i++) {
        for (int j = 0; j < CrunchifyWidth; j++) {
          if (byteMatrix.get(i, j)) {
            graphics.fillRect(i, j, 1, 1);
          }
        }
      }
      // BufferedImage logo = null;
      // String currentUrl=QRCodeGenerator.class.getClassLoader().getResource("").getPath();
      // currentUrl=currentUrl.split("/WEB-INF")[0];
      // //二维码中间小图片
      // String qrImageUrl =currentUrl+ SettingUtils.get().getQrImageUrl();
      // logo = ImageIO.read(new File(qrImageUrl));
      // int logoPosition = (new Double(CrunchifyWidth / 2)).intValue()
      // - (logo.getWidth()) / 2;
      // graphics.drawImage(logo, logoPosition, logoPosition, null);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(image, "jpg", baos);
      baos.flush();
      bytes = baos.toByteArray();
      baos.close();
      return bytes;
    } catch (WriterException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    LogUtil.debug(QRCodeGenerator.class, "generateQrImage",
        "\n\nYou have successfully created QR Code.");
    return bytes;
  }

  public static void main(String[] args) {
    // byte[] bytes = generateQrImage ("http://carlife.chcws.com:8080/upload/apk/csh.apk");
    String content = "{\"flag\":\"" + DigestUtils.md5Hex("翼享生活") + "\",\"sellerId\":\"" + 6 + "\"}";
    byte[] bytes = generateQrImage(content);

    System.out.println(bytes.length);

  }
}

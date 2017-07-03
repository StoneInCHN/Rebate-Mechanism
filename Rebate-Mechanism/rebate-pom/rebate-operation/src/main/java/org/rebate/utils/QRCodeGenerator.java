package org.rebate.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;



public class QRCodeGenerator {
	private String content;
	private String smallImageURL;
	private int size;
	private OutputStream out;
	public QRCodeGenerator(String content, String smallImageURL, Integer size,
			OutputStream out) {
		this.content = content;
		this.smallImageURL = smallImageURL;
		this.size = size;
		this.out = out;
	}
	/**
	 * 生成二维码图片，写入到输出流中
	 * @param content  二维码携带信息
	 * @param smallImageURL   二维码中间logo图片URL
	 * @param out 输出流
	 */
	public void generateQrImage(){
		if (size <= 0) size = 1;
		int imageWidth = 177 * size;
		int logoWidth = 36 * size;
		int line = 4 * size;
		int logoBgWidth = logoWidth + line;
		
		try {
			Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, imageWidth, imageWidth, hintMap);
			int qrWidth = byteMatrix.getWidth();
			BufferedImage qrImage = new BufferedImage(qrWidth, qrWidth, BufferedImage.TYPE_INT_RGB);

			Graphics2D qrGraphics = (Graphics2D) qrImage.createGraphics();
			qrGraphics.setColor(Color.WHITE);
			qrGraphics.fillRect(0, 0, qrWidth, qrWidth);
			qrGraphics.setColor(Color.BLACK);
			for (int i = 0; i < qrWidth; i++) {
				for (int j = 0; j < qrWidth; j++) {
					if (byteMatrix.get(i, j)) {
						qrGraphics.fillRect(i, j, 1, 1);
					}
				}
			}
            
			//二维码中间小图片
            BufferedImage logo = null;
		    logo = ImageIO.read(new File(smallImageURL));
            BufferedImage logoRound = new BufferedImage(logoWidth, logoWidth, BufferedImage.TYPE_INT_ARGB);
            Graphics2D logoRoundGraphics = logoRound.createGraphics();
            logoRoundGraphics.setComposite(AlphaComposite.Src);
            logoRoundGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            logoRoundGraphics.setColor(Color.WHITE);
            logoRoundGraphics.fill(new RoundRectangle2D.Float(0, 0, logoWidth, logoWidth, logoWidth/8, logoWidth/8));
            logoRoundGraphics.setComposite(AlphaComposite.SrcAtop);
            logoRoundGraphics.drawImage(logo, 0, 0, logoWidth, logoWidth, null);
            logoRoundGraphics.dispose();
            
            BufferedImage logoBg = new BufferedImage(logoBgWidth, logoBgWidth, BufferedImage.TYPE_INT_ARGB);
            Graphics2D logoBgGraphics = logoBg.createGraphics();
            logoBgGraphics.setColor(Color.WHITE);
            logoBgGraphics.fill(new RoundRectangle2D.Float(0, 0, logoBgWidth, logoBgWidth, (logoBgWidth)/8, (logoBgWidth)/8));
            logoBgGraphics.drawImage(logoRound, line/2, line/2, logoWidth, logoWidth, null);
            logoBgGraphics.dispose();
            
            int logoPosition = (new Double(qrWidth/2)).intValue() - logoWidth/2;
            qrGraphics.drawImage(logoBg, logoPosition, logoPosition, null);
            qrGraphics.dispose();
            
            //二维码QRCode图片 写入到输出流中  
            ImageIO.write(qrImage, "jpg", out);  
            
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		LogUtil.debug(this.getClass(),"generateQrImage","\n\nYou have successfully created QR Code.");
	}
    public static void main(String[] args) throws IOException {
    	//String content = "{\"flag\":\"" + DigestUtils.md5Hex("翼享生活") + "\",\"sellerId\":\"" + 6 + "\"}";
    	//generateQrImage(content,"E:/seller/seller1.png", 6, new File("E:/seller/seller1_round.png"));
    }
}

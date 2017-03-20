package org.rebate.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FilenameUtils;

public class ImageUtils {

	public static void zoom(File srcFile, File destFile, Integer destWidth, Integer destHeight){
		Graphics2D graphics2D = null;
		ImageOutputStream imageOutputStream = null;
		ImageWriter imageWriter = null;
		try {
			BufferedImage srcBufferedImage = ImageIO.read(srcFile);
			int srcWidth = srcBufferedImage.getWidth();
			int srcHeight = srcBufferedImage.getHeight();
			int width;
			if(destWidth!=null){
			  width = destWidth;
			}else{
			  width=srcWidth;
			}
			int height = destHeight;
			if(destWidth!=null){
			  if (srcHeight >= srcWidth) {
                width = (int) Math.round(((destHeight * 1.0 / srcHeight) * srcWidth));
              }else {
                height = (int) Math.round(((destWidth * 1.0 / srcWidth) * srcHeight));
              }
			}else{
			  destWidth=srcWidth;
			}
			BufferedImage destBufferedImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
			graphics2D = destBufferedImage.createGraphics();
			graphics2D.setBackground(Color.white);
			graphics2D.clearRect(0, 0, destWidth, destHeight);
			graphics2D.drawImage(srcBufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), (destWidth / 2) - (width / 2), (destHeight / 2) - (height / 2), null);

			imageOutputStream = ImageIO.createImageOutputStream(destFile);
			imageWriter = ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(destFile.getName())).next();
			imageWriter.setOutput(imageOutputStream);
			ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
			imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			imageWriteParam.setCompressionQuality((float) (100 / 100.0));
			imageWriter.write(null, new IIOImage(destBufferedImage, null, null), imageWriteParam);
			imageOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (graphics2D != null) {
				graphics2D.dispose();
			}
			if (imageWriter != null) {
				imageWriter.dispose();
			}
			if (imageOutputStream != null) {
				try {
					imageOutputStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

}

package org.rebate.utils.apk;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.rebate.beans.ApkInfo;

public class ApkUtil {
	
	private static final Namespace NS = Namespace.getNamespace("http://schemas.android.com/apk/res/android");
    @SuppressWarnings("unchecked")
    public static synchronized ApkInfo getApkInfo(File f){
        ApkInfo apkInfo = new ApkInfo();
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        try{
            document = builder.build(getXmlInputStream(f));
        }catch (Exception e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();//跟节点-->manifest
        apkInfo.setVersionCode(root.getAttributeValue("versionCode",NS));
        apkInfo.setVersionName(root.getAttributeValue("versionName", NS));
      return apkInfo;
    }	
    private static InputStream getXmlInputStream(File f) {
      InputStream inputStream = null;
      InputStream xmlInputStream = null;
      ZipFile zipFile = null;
      try {
          zipFile = new ZipFile(f);
          ZipEntry zipEntry = new ZipEntry("AndroidManifest.xml");
          inputStream = zipFile.getInputStream(zipEntry);
          AXMLPrinter xmlPrinter = new AXMLPrinter();
          xmlPrinter.startPrinf(inputStream);
          xmlInputStream = new ByteArrayInputStream(xmlPrinter.getBuf().toString().getBytes("UTF-8"));
      } catch (IOException e) {
          e.printStackTrace();
          try {
              inputStream.close();
              zipFile.close();
          } catch (IOException e1) {
              e1.printStackTrace();
          }
      }
      return xmlInputStream;
    }

}

package org.rebate.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 导出数据到excel
 * @author luzhang
 */
public class ExportExcel extends Thread {
  
    private String title;
    private JSONArray jsonArray;
    private List<Map<String, String>> eventRecordMapList;
    private OutputStream out;
    private Object locker;
    public ExportExcel(String title, JSONArray jsonArray, List<Map<String, String>> eventRecordMapList, OutputStream out,Object locker){
        this.title = title;
        this.jsonArray = jsonArray;
        this.eventRecordMapList = eventRecordMapList;
        this.out = out;
        this.locker = locker;
    }
   @Override
   public void run() {
     exportExcel(title, jsonArray, eventRecordMapList, out);//导出excel
     synchronized (this.locker) {
       locker.notify();//导出excel完毕，notify主线程的对象锁
    }
   }
   
   public void exportExcel(String title, JSONArray jsonArray, List<Map<String, String>> eventRecordMapList, OutputStream out) {
      //创建一个excel工作簿
      HSSFWorkbook workbook = new HSSFWorkbook();
      HSSFSheet sheet = workbook.createSheet(title);
      //excel列默认宽度
      //sheet.setDefaultColumnWidth(20);
      
      //第一行标题样式（白字蓝底）
      HSSFCellStyle titleStyle = workbook.createCellStyle();
      HSSFPalette palette = workbook.getCustomPalette();  
      palette.setColorAtIndex((short) 63, (byte) (50), (byte) (126), (byte) (179));
      titleStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
      titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
      titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
      titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      HSSFFont font = workbook.createFont();
      font.setColor(HSSFColor.WHITE.index);
      font.setFontHeightInPoints((short) 12);
      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      titleStyle.setFont(font);
      titleStyle.setWrapText(false);  
      
      //内容行样式   （白底黑字）
      HSSFCellStyle contentStyle = workbook.createCellStyle();      
      contentStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      contentStyle.setFillForegroundColor(HSSFColor.WHITE.index);
      contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
      contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
      contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
      contentStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);     
      HSSFFont font2 = workbook.createFont();
      font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
      contentStyle.setFont(font2);
      contentStyle.setWrapText(true);  
      
      //填充标题,就是第一行啦
      HSSFRow row = sheet.createRow(0);
      row.setHeight((short)500);
      for (short i = 0; i < jsonArray.length(); i++) {
         HSSFCell cell = row.createCell(i);
         //cell.setCellStyle(titleStyle);      
         JSONObject jsonObject = jsonArray.getJSONObject(i);
         HSSFRichTextString text = new HSSFRichTextString(jsonObject.getString("headerName"));
         cell.setCellValue(text);
      }
      
      //填充内容行，就是除第一行外的所有行，从第二行开始
      for (int i = 0; i < eventRecordMapList.size(); i++) {
        row = sheet.createRow(i+1);
        row.setHeight((short)350);
        Map<String, String> eventRecordMap = eventRecordMapList.get(i);
        for (int j = 0; j < jsonArray.length(); j++) {
          JSONObject jsonObject = jsonArray.getJSONObject(j);
           HSSFCell cell = row.createCell(j);
           //cell.setCellStyle(contentStyle);
           try {
               String textValue = eventRecordMap.get(jsonObject.getString("header"));
               if(textValue != null){
                 cell.setCellValue(textValue);
               }
           } catch (Exception e) {
               e.printStackTrace();
           } finally {

           }
        } 
     
      }
      try {
          workbook.write(out);//将excel工作簿写入到输出流中
          //workbook.close();//关闭

      } catch (IOException e) {
         e.printStackTrace();
      }
 
   }
}
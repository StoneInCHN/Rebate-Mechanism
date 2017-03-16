package org.rebate.entity.lucene;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

/**
 * 自定义时间索引格式
 * 
 * @author shijun
 *
 */
public class DateBridgeImpl implements FieldBridge {

  public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {

    if (value instanceof Date) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      luceneOptions.addFieldToDocument(name, sdf.format((Date) value), document);
    }

  }

}

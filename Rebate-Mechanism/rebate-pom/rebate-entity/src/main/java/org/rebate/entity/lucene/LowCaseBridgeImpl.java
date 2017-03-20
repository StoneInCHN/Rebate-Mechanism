package org.rebate.entity.lucene;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

public class LowCaseBridgeImpl implements FieldBridge {

  public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
    if (value instanceof String) {
      String field = ((String) value).toLowerCase();
      luceneOptions.addFieldToDocument(name, field, document);
    }

  }
}

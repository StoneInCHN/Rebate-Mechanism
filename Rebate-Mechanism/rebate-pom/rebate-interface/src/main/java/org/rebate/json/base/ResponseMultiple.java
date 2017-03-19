package org.rebate.json.base;

import java.util.List;

/**
 * 返回entity 结果集
 * @author huyong
 *
 * @param <T>
 */
public class ResponseMultiple<T> extends BaseResponse{

  private List<T> msg;
  
  private PageResponse page;

  public List<T> getMsg() {
    return msg;
  }

  public void setMsg(List<T> msg) {
    this.msg = msg;
  }

  public PageResponse getPage() {
    return page;
  }

  public void setPage(PageResponse page) {
    this.page = page;
  }
  
  
}

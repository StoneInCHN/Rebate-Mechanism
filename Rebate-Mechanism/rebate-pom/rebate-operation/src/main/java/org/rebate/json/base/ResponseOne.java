package org.rebate.json.base;


/**
 * 返回单个entity 结果
 * @author huyong
 *
 * @param <T>
 */
public class ResponseOne<T> extends BaseResponse{

  private T msg;

  public T getMsg() {
    return msg;
  }

  public void setMsg(T msg) {
    this.msg = msg;
  }

  
  
}

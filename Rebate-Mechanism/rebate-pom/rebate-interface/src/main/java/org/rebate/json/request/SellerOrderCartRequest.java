package org.rebate.json.request;

import java.util.List;

import org.rebate.json.base.BaseRequest;

public class SellerOrderCartRequest extends BaseRequest {
  private List<OrderRequest> orderRequests;

  public List<OrderRequest> getOrderRequests() {
    return orderRequests;
  }

  public void setOrderRequests(List<OrderRequest> orderRequests) {
    this.orderRequests = orderRequests;
  }


}

package org.rebate.json.base;

public class PageResponse {
    
      /**
       * 总记录数
       */
      private Integer total;
      
      /**
       * 第几页
       */
      private Integer pageNumber;
      
      /**
       * 每一页数据
       */
      private Integer pageSize;

      public Integer getTotal() {
        return total;
      }

      public void setTotal(Integer total) {
        this.total = total;
      }

      public Integer getPageNumber() {
        return pageNumber;
      }

      public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
      }

      public Integer getPageSize() {
        return pageSize;
      }

      public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
      }
      
}

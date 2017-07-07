package org.rebate.json.response;

import java.util.List;

import org.rebate.json.base.ResponseMultiple;



public class AreaCountResponse<T> {
  /**
   * 地区
   */
  private long id;
  /**
   * 地区名称
   */
  private String name;
  /**
   * 总数
   */
  private long count;
  /**
   * 列表
   */
  private List<T> list;
  
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}


}

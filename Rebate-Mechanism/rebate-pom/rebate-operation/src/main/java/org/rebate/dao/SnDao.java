package org.rebate.dao; 
import org.rebate.entity.Sn.Type;

public interface SnDao{

	  /**
	   * 生成序列号
	   * 
	   * @param type 类型
	   * @return 序列号
	   */
	  String generate(Type type);
}
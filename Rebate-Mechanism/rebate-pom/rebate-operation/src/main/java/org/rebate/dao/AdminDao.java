package org.rebate.dao;

import java.util.List;

import org.rebate.entity.Admin;
import org.rebate.framework.dao.BaseDao;


/**
 * Dao - 管理员
 * @param <Admin>
 * 
 */
public interface AdminDao extends BaseDao<Admin, Long> {

  /**
   * 判断用户名是否存在
   * 
   * @param username 用户名(忽略大小写)
   * @return 用户名是否存在
   */
  boolean usernameExists(String username);

  /**
   * 判断E-mail是否存在
   * 
   * @param email E-mail(忽略大小写)
   * @return E-mail是否存在
   */
  boolean emailExists(String email);

  /**
   * 根据用户名查找管理员
   * 
   * @param username 用户名(忽略大小写)
   * @return 管理员，若不存在则返回null
   */
  Admin findByUsername(String username);

  /**
   * 根据名字查询
   * 
   * @param name
   * @return
   */
  List<Admin> findByName(String name);

  /**
   * 通过名字精确查找
   * 
   * @param name
   * @return
   */
  Admin findByNameAccurate(String name);



}

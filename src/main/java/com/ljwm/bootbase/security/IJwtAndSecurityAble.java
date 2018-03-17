package com.ljwm.bootbase.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Date;

/**
 * JKhaled created by yunqisong@foxmail.com 2017/11/21
 * FOR : JWT 和 SpringSecurity整合的抽象接口
 */
public interface IJwtAndSecurityAble extends UserDetails {

  /**
   * 获取传输对象的ID
   *
   * @return
   */
  <T extends Serializable> T getId();

  /**
   * 获取传输对象上次修改密码的时间
   *
   * @return
   */
  Date getLastModifyPasswordTime();

  /**
   * 获取传输对象在本次登录的类型
   *
   * @return
   */
  String getLoginType();
}

package com.ljwm.bootbase.security;


import cn.hutool.crypto.SecureUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;

/**
 * JKhaled created by yunqisong@foxmail.com 2017/11/21
 * FOR : 获取当前基本信息工具类
 */
public class SecurityKit {

  /**
   * 获取当前用户
   *
   * @return
   */
  public static <T extends IJwtAndSecurityAble> T currentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) return null;
    Object user = authentication.getPrincipal();
    return (T) user;
  }

  /**
   * 当前用户的id
   *
   * @return
   */
  public static <T extends Serializable> T currentId() {
    return currentUser() == null ? null : currentUser().getId();
  }

  /**
   * md5加密封装
   *
   * @param password
   * @param salt
   * @return
   */
  public static String passwordMD5(String password, String salt) {
    return SecureUtil.md5(SecureUtil.md5(password) + salt);
  }

}

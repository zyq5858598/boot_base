package com.ljwm.bootbase.security;

import lombok.extern.slf4j.Slf4j;

/**
 * JKhaled created by yunqisong@foxmail.com 2017/11/21
 * FOR : 线程安全的储存当前用户登录过程中传输的全局关键数据
 */
@Slf4j
public class LoginInfoHolder {

  private static final ThreadLocal<String> passwordHolder = new ThreadLocal<>();

  private static final ThreadLocal<String> loginTypeHolder = new ThreadLocal<>();

  public static void setSalt(String salt) {
    log.debug("当前用户的salt", salt);
    passwordHolder.set(salt);
  }

  public static String getSalt() {
    return passwordHolder.get();
  }

  public static void setLoginType(String loginType) {
    log.debug("当前用户的登陆类型", loginType);
    loginTypeHolder.set(loginType);
  }

  public static String getLoginType() {
    return loginTypeHolder.get();
  }
}

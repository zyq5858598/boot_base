package com.ljwm.bootbase.exception;

import com.ljwm.bootbase.enums.ResultEnum;

/**
 * 后台服务逻辑异常
 * Created by yuzhou on 2018/3/14.
 */
public class LogicException extends RuntimeException {

  private Integer code;
  private String message;

  public LogicException(Integer code, String message) {
    super(message);
    this.code = code;
    this.message = message;
  }

  public LogicException(String message) {
    this(-1, message);
  }

  public LogicException(ResultEnum resultEnum) {
    this(resultEnum.getCode(), resultEnum.getMsg());
  }

  public LogicException(ResultEnum resultEnum, String msg) {
    this(resultEnum.getCode(), resultEnum.getMsg() + ":" + msg);
  }
}

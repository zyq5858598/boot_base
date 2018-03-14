package com.ljwm.bootbase.enums;

/**
 * 后台服务错误代码列表
 * Created by yuzhou on 2018/3/14.
 */
public enum  ResultEnum {

  UNKNOWN_ERROR(-1, "未知错误"),
  DATA_ERROR(1, "数据验证错误"),
  BAD_CREDENTIALS(2, "用户名或密码错误"),
  FAIL_TO_SAVE_FILE(3, "文件保存失败"),
  FAIL_TO_DELETE(4, "删除失败"),
  COMMUNICATION_ERROR(10, "后台通讯异常"),
  SUCCESS(0, "成功");

  private Integer code;
  private String msg;


  ResultEnum(Integer code, String key) {
    this.code = code;
    this.msg = key;
  }

  public String getMsg() {
    return msg;
  }

  public Integer getCode() {
    return code;
  }
}

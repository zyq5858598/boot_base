package com.ljwm.bootbase.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.ljwm.bootbase.enums.ResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * API返回数据
 * Created by yuzhou on 2018/3/14.
 */
@Data
@Accessors(chain = true)
@ApiModel("API返回数据")
public class ServerResponse<T> implements Serializable {

  private int status;
  private String msg;
  private T data;

  @JSONField(serialize = false)
  @ApiModelProperty(hidden = true)
  private Boolean success;

  private ServerResponse(int status) {
    this.status = status;
  }

  private ServerResponse(int status, T data) {
    this.status = status;
    this.data = data;
  }

  private ServerResponse(int status, String msg, T data) {
    this.status = status;
    this.msg = msg;
    this.data = data;
  }

  private ServerResponse(int status, String msg) {
    this.status = status;
    this.msg = msg;
  }

  public static <T> ServerResponse<T> createBySuccess() {
    return new ServerResponse<T>(ResultEnum.SUCCESS.getCode());
  }

  public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
    return new ServerResponse<T>(ResultEnum.SUCCESS.getCode(), msg);
  }
}

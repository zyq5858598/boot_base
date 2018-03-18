package com.ljwm.bootbase.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * JKhaled created by yunqisong@foxmail.com 2017/11/16
 * FOR :  通用查询参数
 */
@Data
@Accessors(chain = true)
public class CommonQuery implements Serializable {

  @ApiModelProperty("模糊查询参数")
  private String text;

  @ApiModelProperty("正序: true 倒序: false")
  private Boolean asc;

  @ApiModelProperty("分页参数")
  private PageQuery page;
}

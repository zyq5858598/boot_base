package com.ljwm.bootbase.dto;

/**
 * Created by yunqisong on 2018/1/19/019.
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by yunqisong on 2017/12/25/025.
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss")
@ApiModel("本项目静态文件所在阿里云OSS的配置")
public class Oss {

  @ApiModelProperty("OSS文件上传接入端点")
  private String endpoint;

  @ApiModelProperty("OSS文件上传接入KEY ID")
  private String accessKeyId;

  @ApiModelProperty("OSS文件上传接入KEY SECRET")
  private String accessKeySecret;

  @ApiModelProperty("本应用文件所属OSS仓库")
  private String bucket;

  @ApiModelProperty("本应用文件所属OSS仓库的公网访问URL")
  private String bucketUrl;

  @ApiModelProperty("本应用文件在所属OSS仓库中根路径")
  private String rootPath;
}


package com.ljwm.bootbase.runner;

import cn.hutool.core.util.StrUtil;
import com.ljwm.bootbase.dto.Oss;
import com.ljwm.bootbase.kit.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by yunqisong on 2017/12/25/025.
 */
@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class OssRunner implements ApplicationRunner {

  @Autowired
  private Oss oss;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("Init oss util with oss configuration: {}", oss);

    if (StrUtil.isNotBlank(oss.getAccessKeyId()) && StrUtil.isNotBlank(oss.getAccessKeyId()) && StrUtil.isNotBlank(oss.getAccessKeySecret()) && StrUtil.isNotBlank(oss.getBucket()))
      // 初始化OSS配置
      OssUtil.me.setOssClient(
        oss.getEndpoint(),
        oss.getAccessKeyId(),
        oss.getAccessKeySecret(),
        oss.getBucket());
  }
}

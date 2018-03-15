package com.ljwm.bootbase.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger 文档配置
 * Created by yunqisong on 2018/3/15/015.
 */
@Data
@Configuration
@EnableSwagger2
@ConfigurationProperties(prefix = "swagger2")
public class Swagger2Config {

  private String basePackage;   //

  private String authorization; // Authorization 的 Header 头

  private String token;         // token 的界面描述

  private String title;         // 标题

  private String description;   // 描述

  private String version;       // 版本

  /**
   * 注入RestConfigBean
   *
   * @return
   */
  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(apiInfo())
      .select()
      .apis(RequestHandlerSelectors.basePackage(basePackage))
      .paths(PathSelectors.any())
      .build()
      .globalOperationParameters(setHeaderToken());
  }

  /**
   * 自动获取token登录
   *
   * @return
   */
  private List<Parameter> setHeaderToken() {
    ParameterBuilder tokenPar = new ParameterBuilder();
    List<Parameter> pars = new ArrayList<>();
    tokenPar.name(authorization).description(token).modelRef(new ModelRef("string")).parameterType("header").required(false).build();
    pars.add(tokenPar.build());
    return pars;
  }

  /**
   * 文档基本描述信息
   *
   * @return
   */
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
      .title(title)
      .description(description)
      .version(version)
      .build();
  }
}

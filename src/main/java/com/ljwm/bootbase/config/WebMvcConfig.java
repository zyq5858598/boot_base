package com.ljwm.bootbase.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.ljwm.bootbase.interceptor.ApiLogInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yuzhou on 2018/3/14.
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

  @Value("${spring.http.converters.preferred-json-mapper}")
  private String [] jsonMappers;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new ApiLogInterceptor()).addPathPatterns("/**");
    super.addInterceptors(registry);
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {}

  private void addFastJsonConverter(List<HttpMessageConverter<?>> converters) {
    // TODO: 不是简单移除掉Jackson converter，而是排在其它json lib的前面
    converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);

    log.info("Add a FastJsonHttpMessageConverter as HttpMessageConverter");
    //定义一个转换消息的对象
    FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
    //添加fastjson的配置信息 比如 ：是否要格式化返回的json数据
    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    fastJsonConfig.setSerializerFeatures(
      SerializerFeature.DisableCircularReferenceDetect,// 拒绝循环引用
      SerializerFeature.WriteMapNullValue,           // 空对象null输出
      SerializerFeature.WriteNullListAsEmpty,        // 空数组转数组
      SerializerFeature.WriteNullBooleanAsFalse,     // 空boolean 转 false
      SerializerFeature.WriteNullStringAsEmpty,      // null字符串 转化为""字符串
      SerializerFeature.WriteEnumUsingName           // 枚举转 调用 name() 而不是toString
    );
    //在转换器中添加配置信息
    fastConverter.setFastJsonConfig(fastJsonConfig);
    //将转换器添加到converters中
    converters.add(fastConverter);
  }

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    if (Arrays.stream(jsonMappers).anyMatch(e -> e.equalsIgnoreCase("fastjson"))) {
      this.addFastJsonConverter(converters);
    }

    log.debug("There has been {} HttpMessageConverters when extend", converters.size());

    converters.forEach(converter -> {
      log.debug("HttpMessageConverter: {}", converter.getClass().getName());
    });
  }
}

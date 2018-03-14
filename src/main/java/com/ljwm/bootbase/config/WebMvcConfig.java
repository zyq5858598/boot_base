package com.ljwm.bootbase.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.ljwm.bootbase.interceptor.ApiLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by yuzhou on 2018/3/14.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {


  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new ApiLogInterceptor()).addPathPatterns("/**");
    super.addInterceptors(registry);
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
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
}

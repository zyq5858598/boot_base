package com.ljwm.bootbase.security;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

/**
 * JKhaled created by yunqisong@foxmail.com 2017/11/21
 * FOR :  Spring-Security 和 jwt 联合配置
 */
@Slf4j
@Configuration
@EnableWebSecurity  // 禁用Spring-Boot 默认的配置，配合@Configuration启动自定义配置
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JwtWebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${jwt.route.authentication.path}")
  private String authPath;

  @Value("${jwt.route.freeRouters}")
  private String freeRouters;

  /**
   * 转载Bcrypt密码解码器
   *
   * @return
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new PasswordEncoder() {
      @Override
      public String encode(CharSequence rawPassword) {
        return SecureUtil.md5(SecureUtil.md5((String) rawPassword) + LoginInfoHolder.getSalt());
      }

      @Override
      public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(SecureUtil.md5(SecureUtil.md5((String) rawPassword) + LoginInfoHolder.getSalt()));
      }
    };
  }

  /**
   * 配置 AuthenticationManagerBuilder
   *
   * @param authenticationManagerBuilder
   * @throws Exception
   */
  @Autowired
  public void configureGlobal(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsServiceImpl,
                              AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder
      .userDetailsService(userDetailsServiceImpl)
      .passwordEncoder(passwordEncoder());

  }


  /**
   * HTTP 401 返回注入
   *
   * @return
   */
  @Bean
  Http401AuthenticationEntryPoint authenticationEntryPoint() {
    return new Http401AuthenticationEntryPoint("");
  }

  /**
   * HTTP 403: 返回注入
   *
   * @return
   */
  @Bean
  Http403ForbiddenEntryPoint http403ForbiddenEntryPoint() {
    return new Http403ForbiddenEntryPoint();
  }

  /**
   * 提供Jwt认证过滤器
   *
   * @return
   */
  @Bean
  public JwtTokenFilter authenticationTokenFilter() {
    return new JwtTokenFilter();
  }


  @Override
  public void configure(WebSecurity web) throws Exception {
    log.debug("Configure web security");
    String[] frees = new String[]{
      "/**/favicon.ico",
      "/socket/**",
      "/swagger-ui.html/**",
      "/swagger-resources/**",
      "/v2/**",
      "/webjars/**",
      "/images/**",
      "/configuration/**",
      "/favicon.ico",
      "/**/*.html",
      "/**/*.css",
      "/**/*.js"
    };
    if (StrUtil.isNotBlank(freeRouters)) {
      String[] freeConfigRouters = freeRouters.split(ReUtil.escape(","));
      frees = ArrayUtil.addAll(frees, freeConfigRouters);  // 数组相加
    }
    // 允许对静态资源的访问
    web.ignoring().antMatchers(frees);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    log.debug("Configure http security");
    String[] frees = new String[]{};
    if (StrUtil.isNotBlank(freeRouters)) {
      String[] freeConfigRouters = freeRouters.split(ReUtil.escape(","));
      frees = ArrayUtil.addAll(frees, freeConfigRouters);  // 数组相加
    }
    http
      // 由于使用的是JWT, 不需要csrf 跨域攻击拦截
      .csrf().disable()
      // 未认证用户的出错处理，返回401
      .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
      .and()
      // 基于token, 所以中不需要 session
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .authorizeRequests()
      // 对于获取token的rest api要允许匿名访问
      .antMatchers(frees).permitAll()
      // 除上面外的所有请求全部需要监权认证
      .anyRequest().authenticated();
    // 添加 JWT filter
    http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    // 禁用缓存
    http.headers().cacheControl();
  }

}

package com.ljwm.bootbase.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * API访问日志
 * Created by yuzhou on 2018/3/14.
 */
@Slf4j
public class ApiLogInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String apiPath = request.getRequestURI();
    String authorization = request.getHeader("Authorization");
    String queryString = request.getQueryString();
    String method = request.getMethod();
    String contentType = request.getContentType();


    log.debug("------------------- API LOG START ------------------------");
    log.debug("PATH: {}", apiPath);
    log.debug("Method: {}", method);

    if (!StringUtils.isEmpty(contentType)) {
      log.debug("Content-Type: {}", contentType);
    }

    if (!StringUtils.isEmpty(authorization)) {
      log.debug("Authorization: {}", authorization);
    }

    if (!StringUtils.isEmpty(queryString)) {
      log.debug("Query String: {}", queryString);
    }

    log.debug("------------------- API LOG END --------------------------");

    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

  }
}

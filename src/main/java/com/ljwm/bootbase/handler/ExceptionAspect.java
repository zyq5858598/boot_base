package com.ljwm.bootbase.handler;

import com.ljwm.bootbase.dto.Result;
import com.ljwm.bootbase.enums.ResultEnum;
import com.ljwm.bootbase.exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.AccessDeniedException;

/**
 * JKhaled created by yunqisong@foxmail.com 2017/11/21
 * FOR : 错误集中处理切面
 */
@Slf4j
@ControllerAdvice
public class ExceptionAspect {

  @org.springframework.web.bind.annotation.ExceptionHandler(value = LogicException.class)
  @ResponseBody
  public Result handleLogicException(LogicException e) {
    log.warn("Logic Exception occur: {}", e.getMessage());
    return Result.fail(e);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(value = LockedException.class)
  @ResponseBody
  public Result handleLockedException(LockedException e) {
    log.warn("Logic Exception occur: {}", e.getMessage());
    return Result.fail("账号已被禁用!");
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
  @ResponseBody
  public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    log.warn("Logic Exception occur: {}", e.getMessage());
    return Result.fail(e.getMessage().contains("POST") ? ResultEnum.METHOD_ERROR_POST : ResultEnum.METHOD_ERROR_GET);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(value = HttpMessageNotReadableException.class)
  @ResponseBody
  public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    log.warn("Logic Exception occur: {}", e.getMessage());
    return Result.fail(ResultEnum.DATA_ERROR.getCode(), "提交的JSON数据不满足指定的JSON格式请校验后再提交!");
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(value = UsernameNotFoundException.class)
  @ResponseBody
  public Result handleUsernameNotFoundException(UsernameNotFoundException e) {
    log.warn("Logic Exception occur: {}", e.getMessage());
    return Result.fail(ResultEnum.BAD_CREDENTIALS.getCode(), ResultEnum.BAD_CREDENTIALS.getMsg());
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(value = BadCredentialsException.class)
  @ResponseBody
  public Result handleBadCredentialsException(BadCredentialsException e) {
    log.warn("BadCredentialsException occur: {}", e.getMessage());
    return Result.fail(ResultEnum.BAD_CREDENTIALS.getCode(), ResultEnum.BAD_CREDENTIALS.getMsg());
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(value = MethodArgumentNotValidException.class)
  @ResponseBody
  public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.warn("MethodArgumentNotValidException occur: {}", e.getMessage());
    return Result.fail(ResultEnum.DATA_ERROR.getCode(), e.getBindingResult().getFieldError().getDefaultMessage());
  }


  @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
  @ResponseBody
  public Result handleException(Exception e) throws AccessDeniedException {
    if (e instanceof AccessDeniedException) { // 权限访问异常不拦截
      throw (AccessDeniedException) e;
    }
    log.error("Unknown Exception occur", e);
    return Result.fail(ResultEnum.UNKNOWN_ERROR.getCode(), "系统异常!");
  }
}

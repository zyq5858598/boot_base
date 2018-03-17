package com.ljwm.bootbase.controller;

import com.ljwm.bootbase.dto.Result;
import com.ljwm.bootbase.enums.ResultEnum;
import com.ljwm.bootbase.exception.LogicException;

/**
 * Created by yuzhou on 2018/3/14.
 */
public abstract class BaseController {


  /**
   * 基本返回成功信息
   *
   * @return
   */
  protected <T> Result success(T data) {
    return Result.success(data);
  }

  /**
   * 缺省返回成功信息
   *
   * @return
   */
  protected Result success() {

    return success("成功");
  }

  /**
   * 错误返回
   *
   * @param code
   * @param msg
   * @return
   */
  protected Result fail(Integer code, String msg) {

    return Result.fail(code,msg);
  }

  /**
   * 枚举直接构造错误
   *
   * @param resultEnum
   * @return
   */
  protected Result fail(ResultEnum resultEnum) {

    return fail(resultEnum.getCode(), resultEnum.getMsg());
  }

  /**
   * 异常构造错误
   *
   * @param logicException
   * @return
   */
  protected Result fail(LogicException logicException) {

    return fail(logicException.getCode(), logicException.getMessage());
  }


}

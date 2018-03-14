package com.ljwm.bootbase.controller;

import com.ljwm.bootbase.dto.ServerResponse;

/**
 * Created by yuzhou on 2018/3/14.
 */
public class BaseController {

  /**
   * 缺省返回成功信息
   *
   * @return
   */
  protected ServerResponse success() {
    return ServerResponse.createBySuccessMessage("成功");
  }


}

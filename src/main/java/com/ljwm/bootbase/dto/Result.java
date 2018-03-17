package com.ljwm.bootbase.dto;

import com.ljwm.bootbase.enums.ResultEnum;
import com.ljwm.bootbase.exception.LogicException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by yunqisong on 2018/2/6/006.
 */
@Data
@Accessors(chain = true)
@SuppressWarnings("unchecked")
public class Result<T> {

    @ApiModelProperty("请求状态码")
    private Integer code;

    @ApiModelProperty("错误MSG")
    private String msg;

    @ApiModelProperty("成功数据体")
    private T data;



    public static <T> Result<T> success(T data) {

        return new Result().setCode(0).setData(data).setMsg("成功");
    }

    public static <T> Result<T> fail(String msg) {

        return fail(ResultEnum.DATA_ERROR.getCode(), msg);
    }

    public static <T> Result<T> fail(Integer code, String msg) {

        return new Result().setCode(code).setMsg(msg);
    }

    public static <T> Result<T> fail(ResultEnum resultEnum) {

        return fail(resultEnum.getCode(), resultEnum.getMsg());
    }

    public static <T> Result<T> fail(ResultEnum resultEnum, String msg) {

        return fail(resultEnum.getCode(), msg);
    }

    public static <T> Result<T> fail(LogicException e) {

        return fail(e.getCode(), e.getMessage());
    }


}

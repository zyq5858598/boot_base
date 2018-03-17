package com.ljwm.bootbase.dto;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * JKhaled created by yunqisong@foxmail.com 2017/11/16
 * FOR :  查询分页参数
 */
@Data
@Accessors(chain = true)
@ApiModel("分页参数")
@NoArgsConstructor
@AllArgsConstructor
public class PageQuery implements Serializable {

    @ApiModelProperty("当前页 第0页等于第1页")
    private Integer current;

    @ApiModelProperty("页大小")
    private Integer size;

    public Pagination toPage() {
        return new Pagination(current == null ? 1 : current, size == null ? 10 : size);
    }

}

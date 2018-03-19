package com.ljwm.bootbase.ext;

import cn.hutool.core.util.StrUtil;
import com.ljwm.bootbase.exception.LogicException;
import com.ljwm.bootbase.kit.PathKit;
import lombok.Builder;
import lombok.Data;

/**
 * JKhaled created by yunqisong@foxmail.com 2017/11/16
 * FOR : 项目Generator配置类
 */
@Data
@Builder
public class GeneratorConfig {

  @Builder.Default
  private String authorName = "User";                   // 作者名: 默认: User

  @Builder.Default
  private String prefix = "t_";                         // 表名前缀: 默认: t_

  @Builder.Default
  private String path = PathKit.getProjectPath() + "/"; // 生成文件src之前的路径: 默认项目根路径

  private String[] tables;                              // 要生成的表名: 必传参数

  private String url;                                   // JDBC数据库URL: 必传参数

  private String password;                              // JDBC数据库密码: 必传参数

  private String username;                              // JDBC数据库用户名: 必传参数

  private String basePackage;                           // 项目的基本包名，如:com.ljwm.asmshop : 必传参数

  @Builder.Default
  private Boolean created = false;                      // 建造还是更新: 建造会创建Mapper，更新只更新Model和XML

  /**
   * 测试配置构造是否规范
   */
  public void allowed() {
    String temp = "Generator构造参数不可缺少:";
    if (StrUtil.isAllBlank(tables))
      throw new LogicException(temp + "要生成的表名!");
    if (StrUtil.isBlank(url))
      throw new LogicException(temp + "JDBC数据库URL!");
    if (StrUtil.isBlank(password))
      throw new LogicException(temp + "JDBC数据库密码!");
    if (StrUtil.isBlank(username))
      throw new LogicException(temp + "JDBC数据库用户名!");
    if (StrUtil.isBlank(basePackage))
      throw new LogicException(temp + "项目的基本包名!");
  }

}

package com.ljwm.bootbase.ext;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * JKhaled created by yunqisong@foxmail.com 2017/11/21
 * FOR : Mybatis-Generator 拓展
 */
public class MyGenerator extends AutoGenerator {

  private StrategyConfig strategyConfig;

  public MyGenerator(StrategyConfig strategyConfig) {
    this.strategyConfig = strategyConfig;
  }

  public static void build(GeneratorConfig generatorConfig) {
    // 校验构造的参数
    generatorConfig.allowed();
    // 包
    String basePackage = generatorConfig.getBasePackage();
    // 数据源生成
    DataSourceConfig dataSourceConfig =  // 数据源配置
      new DataSourceConfig()
        .setDbType(DbType.MYSQL)            // 数据库类型
        .setDriverName("com.mysql.jdbc.Driver")
        .setUsername(generatorConfig.getUsername())
        .setPassword(generatorConfig.getPassword())
        .setUrl(generatorConfig.getUrl());
    // 自定义需要填充的字段
    List<TableFill> tableFillList = new ArrayList<>();
    tableFillList.add(new TableFill("ASDD_SS", FieldFill.INSERT_UPDATE));
    // 代码生成器
    StrategyConfig strategyConfig = new StrategyConfig()
      .setTablePrefix(generatorConfig.getPrefix())    // 此处可以修改为您的表前缀
      .setNaming(NamingStrategy.underline_to_camel)   // 表名生成策略
      .setInclude(generatorConfig.getTables())        // 需要生成的表
      .setRestControllerStyle(true)
      .setTableFillList(tableFillList)
      .setEntityBuilderModel(true)
      .setEntityLombokModel(true)
      .setEntityColumnConstant(true);

    AutoGenerator mpg = new MyGenerator(strategyConfig).setGlobalConfig(
      // 全局配置
      new GlobalConfig()
        .setOutputDir(generatorConfig.getPath() + "src/main/java")//输出目录
        .setFileOverride(true)// 是否覆盖文件
        .setActiveRecord(true)// 开启 activeRecord 模式
        .setEnableCache(false)// XML 二级缓存
        .setBaseResultMap(true)// XML ResultMap
        .setBaseColumnList(true)// XML columList
        .setOpen(false)         // 生成后打开文件夹
        .setAuthor(generatorConfig.getAuthorName())
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        .setMapperName("%sMapper")
        .setXmlName("%sMapper")
        .setServiceName("%sService")
        .setServiceImplName("%sServiceImpl")
        .setControllerName("%sController")
    ).setDataSource(dataSourceConfig)
      .setCfg(
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
        new InjectionConfig() {
          @Override
          public void initMap() {

          }
        }.setFileOutConfigList(Lists.newArrayList(
          new FileOutConfig("/templates/mapper.xml.vm") {
            // 自定义输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
              return generatorConfig.getPath() + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper.xml";
            }
          }
        ))
      ).setStrategy(
        // 策略配置
        strategyConfig
      ).setPackageInfo(
        // 包配置
        new PackageConfig()
          .setEntity("entity")
          .setParent(basePackage)// 自定义包路径
      );
    // 关闭默认 xml 生成，调整生成 至 根目录
    mpg.setTemplate(
      new TemplateConfig()
        // 采用自定义Swagger规范的Model模板
        .setEntity("/templates/myModel.java.vm")
        // 建造模式还是更新模式
        .setMapper(generatorConfig.getCreated() ? ConstVal.TEMPLATE_MAPPER : null)
        // 上面已经重定向XML所以下面不用生成
        .setXml(null)
        // 不生成Controller，Service，ServiceImpl
        .setController(null)
        .setService(null)
        .setServiceImpl(null)
    );
    // 执行生成
    mpg.execute();
  }

}


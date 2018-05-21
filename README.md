# 基于Spring Boot的服务端基类项目


使用说明: 版本 1.2.0 -可以使用任意数据库
  1. DB 模块配置
    spring:
      datasource:
        dbType: mysql    # 可选配置，默认为mysql
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://127.0.0.1:3306/asm_shop_local?characterEncoding=UTF-8&autoReconnect=true
        username: root
        password: 123456
  2. swagger配置
    swagger2:
      basePackage: "com.ljwm"
      authorization: "Authorization"
      title: "XXX项目后台接口文档"
      description: "XXXX"
      version: "V1.0"
  3. JWT 配置
    jwt:
      route:
        freeRouters: "/auth/login,/auth/code"
  4. 启动类配置（注意修改项目路径）
    @SpringBootApplication(scanBasePackages = {"com.ljwm.bootbase.*", "com.ljwm.asmshop.*"})
    @MapperScan(value = {"com.ljwm.asmshop.mapper*"})                           // 定义mapper，给Mybatis去实现代理类
    @EnableAspectJAutoProxy(proxyTargetClass = true)                            // 允许使用自定义JDK代理切面
    @EnableTransactionManagement                                                // 开启事务
    @ServletComponentScan

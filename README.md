# toutiao
仿照今日头条的主页toutiao.com做的一个Java web项目。使用SpringBoot+Mybatis+velocity开发。开发工具和Java语言介绍

### 内容包括：

- 前期准备

- 用户注册登录管理以及使用token

- 资讯发布，图片上传，资讯首页(上传图片至云存储)

- 评论中心，站内信

- Redis入门以及Redis实现赞踩功能

- 异步设计和站内邮件通知系统

- 多种资讯排序算法


## 基本流程

```
创建git仓库，本地配置idea并测试pull和push。
创建SpringBoot工程，导入Web，Velocity和Aop的包。
生成Maven项目，pom.xml包含上述依赖。
```    

```
创建基本的controller，service和model层。
    
controller中使用注解配置，requestmapping，responsebody基本可以解决请求转发以及响应内容的渲染。
    
使用pathvariable和requestparam传递参数。
    
使用velocity编写页面模板，注意其中的语法使用。常用$!{}和${}
    
使用http规范下的httpservletrequest和httpservletresponse来封装请求和相响应，使用封装好的session和cookie对象。
    
使用重定向的redirectview和统一异常处理器exceptionhandler
```    

## 前期准备

**AOP和IOC**
```
IOC解决对象实例化以及依赖传递问题，解耦。
AOP解决纵向切面问题，主要实现日志和权限控制功能。
aspect实现切面，并且使用logger来记录日志，用该切面的切面方法来监听controller。
```
**配置**
```
springboot使用1.4.0
mybatis-spring-boot-starter使用1.2.1
mysql-connector-java使用5.1.6
写好静态文件html css和js。并且注意需要配置
spring.velocity.suffix=.html 保证跳转请求转发到html上
spring.velocity.toolbox-config-location=toolbox.xml
```
    
**两个小工具**
```
ViewObject:方便传递任何数据到
VelocityDateTool:velocity自带工具类
```

    

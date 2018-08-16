# toutiao
仿照今日头条的主页toutiao.com做的一个Java web项目。使用SpringBoot+Mybatis+velocity开发。

### 内容包括：

- 前期准备

- 用户注册登录管理以及使用token

- 资讯发布，图片上传，资讯首页(上传图片至云存储)

- 评论中心，站内信

- Redis入门以及Redis实现赞踩功能

- 异步设计和站内邮件通知系统



## 前期准备

```
创建git仓库，本地配置idea并测试pull和push。
创建SpringBoot工程，导入Web，Velocity和Aop的包。
生成Maven项目，pom.xml包含上述依赖。

AOP和IOC
IOC解决对象实例化以及依赖传递问题，解耦。
AOP解决纵向切面问题，主要实现日志和权限控制功能。
aspect实现切面，并且使用logger来记录日志，用该切面的切面方法来监听controller。

配置
springboot使用1.4.0
mybatis-spring-boot-starter使用1.2.1
mysql-connector-java使用5.1.6
写好静态文件html css和js。并且注意需要配置
spring.velocity.suffix=.html 保证跳转请求转发到html上
spring.velocity.toolbox-config-location=toolbox.xml
   
两个小工具
ViewObject:方便传递任何数据到
VelocityDateTool:velocity自带工具类

```    

**基本流程**
```
创建基本的controller，service和model层。
    
controller中使用注解配置，requestmapping，responsebody基本可以解决请求转发以及响应内容的渲染。
    
使用pathvariable和requestparam传递参数。
    
使用velocity编写页面模板，注意其中的语法使用。常用$!{}和${}
    
使用http规范下的httpservletrequest和httpservletresponse来封装请求和相响应，使用封装好的session和cookie对象。
    
使用重定向的redirectview和统一异常处理器exceptionhandler
```    



## 数据库表

```
    user(is, name, passowrd, salt, head_url)
    news(id, title, link, image, like_count, comment_count, create_date, user_id)
    login_ticket(id ,user_id, ticket, expired, status)
    comment(id, content, user_id, entity_id, entity_type, create_date, status)
    message(id, from_id, to_id, content, create_date, has_read, conversation_id)
```

## 用户注册登录与Token

**UserController**
```
    数据库表Login_ticket来存储ticket字段，每次客户端登录或注册时服务器会下发一个ticket下发给客户端，服务器生成
的这个ticket是根据用户id随机生成的UUid，过期时间以及有效状态，而客户端则将这个ticket设置为Cookies，这样客户端每
次访问页面都是一个带token的http请求（登录状态下的访问页面）。

    使用拦截器HandlerInterceptor可以用来拦截所有用户请求，来判断是否有效的ticket，有的话则写入ThreadLocal，供全
局任意位置获取用户信息。在处理前来判断是否有效，在渲染前把数据装入model供前端页面渲染，在所有处理完后清除ThreadLocal
里本地的用户。写完拦截器后需要在该条链路上注册该拦截器，这样在执行该条链路时就可以回调了。
    
    该ticket功能类似session，通过cookie写回浏览器，浏览器请求再通过cookie传递，区别是该字段在数据库中，并且可
以用在移动端。

    工具类的使用：Json工具类，md5加密为密码加salt，Json数据传输。
   
    数据安全性保障方法：https使用公钥加密私钥解密，比如支付宝的密码加密，单点登录验证，验证码机制等。

```

## 图片上传与云SDK存储

**NewsController**
```
    图片的本地上传：post请求，content-type类型是multipart类型，当有多个图片或参数时，post会使用content-type的
boundary来分割开，后端Spring接受用同样参数来接受File.图片的一般上传流程，比较简单。最后返回的是这个图片的URI。

    图片的展示，如果用get获取时，一般展示的是页面，而要读取图片的话（二进制流），则添加一个头Content-type为image
，来告诉客户端是一个image，有包装好的工具类StreamUtils.copy(Input, Response.outputStream)。

    使用云SDK上传图片，比较容易实现，好处在于CDN内容分发，缩图服务等。这样每次网站发布就不需要加载静态文件了。
```


## 评论和消息中心

**NewsController,MessageController**
```
    对于评论表的字段设计，要考虑到以后的复用，不仅仅是评论资讯，而是评论本身呢？
    资讯的显示评论，通过该资讯id来中查找评论，并按时间顺序打印显示出来。
    
    消息中心：有两个页面，一个消息列表页(所有conversation_id)，一个消息详情页（同一个conversation_id）。
消息通信是两个用户之间发一条消息，把from_id和to_id按从小到大排好序，通过该from-to来存消息。
消息列表页是一个复杂的SQL语句，要求分组后的conversation_id取到最新的那条，并依次按最新的输出。
消息详情页是只需要展示同一个conversation_id的Message就可以了。
    
    并没有新增技术点，前后交互的业务逻辑比较复杂。
```


## 点赞点踩功能

**LikeController**
```
   熟悉Redis基本的数据结构，知道使用Jedis api等。了解Redis在微博中大量应用。
   
   创建Redis池，将Jedis的api封装好成工具，供其他层调用。
   
   根据需求来确定key字段，每一条资讯都有一个like集合和dislike集合，这样分别存userid。
   查询直接在Reis上查找，而不用每次都去刷数据库，缓解了数据库的压力，在一定时间内，再去刷新数据库中的likecount字段。
```


## 异步框架

**async**
```
   大的服务项目要必须是要实现服务化，异步化~ 这两者是同步实行的。当一个事件或者卡IO，并且并不需要实时显示，可以把其生
成一个事件，并抛给队列，并不需要立即实现，节约了时间。如点了一个赞，后续可能是积分增加，站内通知，积分排行榜一些列事件
都需要变化，但是用户只需要知道成功点了一个赞，后续操作可以延迟一些。这就是异步的需要实现的东西了。

   Redis的list可以当成一个事件队列，将对象事件放进去取出来必然要涉及到事件的序列化。进而用EventModel来包装一个事件，开
发中事件生产抛给队列，而消费则从队列取时间，不同的事件有实现不同的EventHandler，类似消费-生产模式。注意:消费者专门用一个
线程去处理，这个线程是阻塞的（当队列没有会一直等待）。

   代码实现：在发生突发情况，队列中的事件还是存在的，消费者在初始化时要把在队列中的事件给整理成一个类似查找表。

   添加了邮件发送功能，引用mail依赖，并配置好自己的邮箱配置，写在业务逻辑代码中。
   
   了解了Hacker News，Reddit，StackOverflow，IMDB的一些排序算法~
```

## 第一天

### 配置环境

首先搭建好了基本的开发环境，让SpringBoot能够正常启动

### 数据库表的设计

然后开始按照任务书上的来设计数据库的表，这张表是按照阿里巴巴的规范来设计的（看gmt_create和gmt_modified就知道了），然后考虑到有数十亿的用户，我用了bigint unsigned来作为用户的ID，同时考虑到删除后会导致用户的信息直接没有，我采用了软删除，让这位用户还有机会恢复他/她的账号（太好了！不用考虑手残误删了！！）而且直接删除数据会导致数据碎片化，对查询性能产生一定的负面影响，所以还是能少删就少删吧，至于gmt_create和gmt_modified，我本来是准备用TIMESTAMP的，但是一看到任务书的2233年，再看看TIMESTAMP只到2038-01-09（本来是想相信甲骨文公司的，但是为了稳妥起见，还是用了datetime来表示时间，就是以后我们可能处理时区的转换有一些麻烦），至于为什么都是16，32，64那是因为2的次方数便于我们的CPU处理，同时可以内存对齐，性能会强一丢丢，同时还创建了索引，提高我们的SQL语句的查询效率，user_id（主键自增）,gmt_create和gmt_modified全部用的都是自动化填充数据。免得我们后端写一些重复代码，重复开发，设置默认值，因为NULL字段很难查询优化、NULL字段的索引需要额外空间，而且NULL字段的复合索引无效，嗯，就是影响性能，所以我设置了默认值

### 正式开始开发

为了充分实现解耦，我设置了config包来进行项目的配置（比如注入了一个Bean），设置了controller层，返回的是Result对象，便于我们的前端兄弟处理（嗯，没前端，就当时给我自己看的更方便吧），同时对于项目中出现的常量统一放到utils包的Constants类里面进行管理，这样更加的规范（当然，对于只在那一个类里面的常量，我就只放在了那个具体的类里面，比如UploadUtil类，其他的类不会用到这些字段，所以我就没有把它放到Constants类里，毕竟我也不想这些字段到处跑，得严格的限制范围，变量就像是自己的小孩，得要让他们呆在我们的视线范围内才行，不然作用域太大，变量无限制的乱跑，我们可是会担心的啊），至于分层领域模型规约，什么VO,DTO,PO,POJO的就不赘述了，一些重要的方法写了JavaDoc，便于我们开发，至于数据库的密码存储，本来是想用慢hash的，但是这么多的用户（任务书上说有数十亿），还是算了，那可能对性能有很大的影响，不如MD5撒盐（宝塔面板也用的是这，嗯，安全性只能说是一般般，凑合着用吧，其实这一点不如让服务器更耐扛一点，毕竟服务器一旦被攻破，什么盐值之类的信息都得泄漏，安全性没有一点），发送邮件用的是QQ的邮件服务器，顺便写了简单的邮件内容，写了一些逻辑，60秒才能注册一次（防止被滥刷）图片本来是准备用Base64的，但是感觉太笨重了（1M还扩容到了1.33M），又考虑了本地存储，但是这对于整个的服务扩展性很不友好（想想微服务），所以最后采用了OSS来存储，还有就是在数据进入我们的Service层之前我们要判断一下数据是不是合法的，不要让一些数据污染了数据库，还有就是我们要对返回给前端的数据注意要脱敏，同时也不要什么数据都返回给前端，前端在发送请求的时候要注意CORS的问题（因为端口号不一样）

## 第二天

### PLUS：今天下午基本上都是在写前端代码（明天会进行调整）
为了使得我们的产品更加的完美（总不可能让用户去敲命令行curl吧），所以我分别实现了登录页面，用户页面，注册页面三个页面的设计，同时可以使这些页面互相之间可以跳转，比如用户在登录页面可以跳转到注册页面去先注册一下（这和我们现实生活中的登录页面功能类似，对于用户更加的友好），还实现了在线修改图片的功能（这一点灵感是我B站换头像的时候想到的，过程比较的艰辛，码前端代码码的比较的多），当然还有一些功能比如找回密码，在线修改名字，改昵称或者是注销账号，显示用户的关注列表这些功能我还都没有做，现在看来我还是以OAuth2的开发为主吧（这些以后再做吧）

接下来谈谈我写代码时候的思考吧（还没有写，只是一些想法，都写的话时间肯定不够）

想到B站违禁评论删评，我便想到bio是不是都是可以放出来的，我们是不是可以设置敏感词去过滤一下呢？

方案如下：

1. elasticsearch来对评论进行关键词搜索，查到直接删评
2. AC自动机建树（就是每一次有新的敏感词建树比较的麻烦）
3. regex + 词库实现
4. 百度智能云API

我们可以对用户设定一个违规值（black值，到时候我们的表可以改进一下），再搞一个黑白名单，达到5之后就会被警告（被发小邮件） 到10就被封号7天的逻辑

有嫌疑的bio让我们的人工来审核，比如可能会有用户发表CN。M，有CNM的嫌疑但是不能直接判定，所以要让我们的人工去审核，到时候可以完善一下（现在先写OAuth2吧）

## 第三天

这天特别忙（课很满），我在晚上抽时间大致实现了阶段2的OAuth2的一半的基本功能

## 第四天

今天我完成了阶段2，3，5（至于4，感觉OIDC有一点难，所以我先跳了，明天再写OIDC）

OAuth2没什么好说的，直接看官方文档了解基本的流程，具体参数的作用，我们就可以开发了

在写阶段5时，我看到一开始直接就收藏，感觉逻辑上面不是特别通（没有动漫，何来收藏一说），动漫的信息从哪来，我们是不是应该先有增加动漫的接口，然后才可以有这么多动漫的信息去让我们收藏？虽然不用实现，但是我觉得这个还是很有必要的

这个整体的动漫逻辑架构一定要完整。这里我就不设置过滤器了，要相信工作人员不会对我们的动漫信息做出奇怪的事情

但是我们要注意在增加动漫之前，由于可能是多个人合作（领导给员工分配任务），所以可能会重复增加，我们要先确认有没有这个动漫之后才能再往里面加

联想到以前追番的时候， 想了一下，其实修改动漫的信息一般我们也不用很麻烦，我们只用修改动漫的集数就行了（因为动漫更新的时候似乎就只有集数会发生变化），这样想来修改动漫的信息比修改用户的信息要方便的多

对了，我们更新，查询动漫的时候要注意满足is_deleted=0的条件，因为我在删除动漫的时候采用的是软删除的方法，所以一定要注意，要不然我们会查询到已经删除的过时信息

至于阶段5的进阶内容，感觉也是差了一点东西，所以我又补充了一点自己的私货

一个具体的流程如下

- 首先user1要申请好友才行，这是第一步
- 别人(user1)申请之后，你(user2)可以在你的信息里面收到，显示有谁想成为你的好友
- 你(user2)同意之后，你们之间的status会变为1，拒绝后你们之间的status为-1

然后才会到我们任务书上的`好友在收藏或评价番剧时，用户都能看到对应信息`这个阶段

对于这个阶段，我们只用做好查询就够了


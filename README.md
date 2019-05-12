# 12306全自动抢票Java版

### 项目重构中 ...
## 项目说明
这个项目的起因是学Java没有合适的练手项目，然后想起之前用Python写的关于12306抢票的程序，所以用Java重写了一遍。<br>
因为这是我写的第一个Java程序，代码质量并不是很好，大家将就着看。
## 使用方法
在 config.properties 文件里配置
格式参见：
+ __12306__
    +     username12306      --> xxx            12306网站用户名
          password12306      --> xxx            12306网站密码
+ **抢票信息**
    +     bookingAfterTime   --> 09:00          选择列车的时间范围
          bookingBeforeTime  --> 12:00
          trainDate          --> 2019-01-31     列车日期
          fromStation        --> 西安            出发站
          toStation          --> 运城            到达站
          purposeCode        --> ADULT          这个默认成人票
          userSetTrainName   --> D2568          列车号，如果未空则取符合条件的最早一班
          passengerName      --> xxx            乘客姓名
          documentType       --> 1              证件类型，1 是身份证， B 是护照，
          documentNumber     --> 123456         证件号
          mobile             --> 11111111111    手机号
          seatType           --> 一等座，二等座   座位类型，可以包含多个
          expectSeatNumber   --> A              期望的座位号，A,B,C,E,F  A 和 F 靠窗，这个不一定能选上
+ **发送通知**
    + **发件方式**，支持3种方式，email，短信，语音（打电话）
    +     notifyMode        --->    email, sms, phone   可以选择三种方式
    + **邮箱设置**
    +     receiverEmail     --->    XXX@gmail.com       收件人，可以有多个
          senderEmail       --->    XXX@gmail.com       发件邮箱
          senderHost        --->    smtp.gmail.com      发件Smtp服务器
          senderPort        --->    465                 发件端口
          senderUsername    --->    XXX                 发件邮箱用户名
          senderPassword    --->    XXX                 发件邮箱密码
    + **短信和语音设置**
    - 笔者封装了twilio平台的使用方式，用其他平台的可以自己重构
    +     phoneSmsPlatformName ---> twilio              提供短信和语音平台的名字
          accountSid           ---> XXX                 twilio 提供
          authToken            ---> XXX                 twilio 提供
          from                 ---> +8612312312312      twilio 提供的电话号码，每个平台格式不一样，twilio格式为：+8612312312312
          tos                  ---> +8612312312312      接收通知的号码，可以选多个，每个平台格式不一样，twilio格式为：+8612312312312
          soundXMLURL          ---> http://www.xxx.com/xxx.xml
          soundXMLURL 是一个url，指向一个xml文件，具体内容参见Twilio开发者文档
    + **Twilio 注意事项**：
      1. 注意电话前要加国家号，例如中国为：+86 以及注意是否有发送到目标号码的权限（从Twilio网站里设置）

## 更新说明
+ 20190126 --> 更改单线程为多线程查询
   + 优化逻辑
   + 可以选择多个座位类型
   + 修复已知的订票失败问题
   
+ 20190122 --> 增加短信通知和语音通知
   + 事情的起因是我跑了4天终于抢到了一张回家的票，但是因为当时我在睡觉没看到邮件，所以因为没有及时付款导致票又没了wtf？？？
   + 所以痛定思痛，我尝试找了短信接口和语音通知接口，当然这种接口一般不是免费的。最开始我在注意到了阿里云，但是阿里云的短信接口和一些第三方的短信接口发送需要联系客服设置模板，甚是麻烦，所以我最后选用了Twilio的通知服务，新用户注册送15美元体验金，足够用了。

## 运行效果
查询以及订票<br>
![runImg](/img/1.png)<br>
发送邮件通知<br>
![emailImg](/img/2.jpg)<br>
短信通知<br>
<img src="/img/sms.jpg" width="50%" height="50%"><br>
语音通知<br>
<img src="/img/phone.jpg" width="50%" height="50%"><br>

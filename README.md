# 12306全自动抢票Java版 :relaxed:


## 项目说明
12306 抢票系列 Java 版。

## 使用方法
你需要在resources文件夹下创建一个配置文件

|文件名|说明|
|----|----|
|configuration.yml|项目配置文件|

然后启动scheduler包里的Scheduler即可
### 配置文件内容
 * configuration.yml
 * ```yaml
     user:
       username12306:        你的12306用户名
       password12306:        你的12306密码
       
     platform:
       config:
         twilio:
           accountSid:       从接口提供商获取
           authToken:        从接口提供商获取
           voiceUrl:         指向一个xml文件，xml文件内容里有一个url指向播放音频文件，详情参见接口提供商文档。
           defaultVoiceUrl:  这个好像没啥用，我测试能不能收到通知用的
           fromPhoneNumber:  从接口提供商获取
         nexmo:
           apiKey:           从接口提供商获取
           apiSecret:        从接口提供商获取
         yunzhixin:
           appCode:          从接口提供商获取
           templateId:       从接口提供商获取
           
     ticket:
       afterTime:            选择列车的时间范围
       beforeTime:           选择列车的时间范围
       trainDate:            列车日期，格式：XXXX-XX-XX
       fromStation:          出发站
       toStation:            到达站
       purposeCode:          默认成人票，请填写："ADULT"
       trainName:            如果没有指定列车号，例如："D2323"，请填写空值：""，但是不能空着不填。空着不填返回Null报错
       backTrainDate:        填写空值即可：""
       passengerName:        乘车人姓名
       documentType:         证件类型，身份证请填写："1"
       documentNumber:       证件号
       mobile:               手机号
       seatType:             座位类型，可以填写多个用英文逗号分割。例如：一等座, 二等座
       expectSeatNumber:     期望的座位号，A,B,C,E,F  A 和 F 靠窗，这个不一定能选上
       
     notification:
       mode:                 接收通知的方式，例如："email, sms, phone"
       receiverPhone:        接收通知的手机号，如果只选择了email接收方式，这个随便填即可，但不能空着
       config:
         phone:
           #interface name is capitalized
           interfaceName: TWILIO
         sms:
           #interface name is capitalized
           interfaceName: NEXMO
           title: "12306Ticket"
           content: "Congratulations! grab the ticket, please log in to 12306 to pay."
           defaultContent: "if you can look this message, then you can receiver notification, from 12306 grab ticket system."
         email:
           receiverEmail:        接收通知的邮箱，例如："xxx@gmail.com"
           senderEmail:          发送通知的邮箱，例如："xxx@gmail.com"
           senderEmailHost:      发送通知的邮箱主机，例如："smtp.gmail.com"
           senderEmailPort:      接收通知的邮箱端口，例如："465"
           senderEmailUsername:  接收通知的邮箱用户名，例如："xxx"
           senderEmailPassword:  接收通知的邮箱密码，例如："xxx"
           defaultTitle: "12306 grab ticket notification"
           defaultContent: "12306 grab ticket notification test, if you can look this email, then you can receive notification."
     
     setting:
       # 启动抢票代码
       grabTicketCode: true
     
   ```
## 更新说明
+ 20190527 --> 项目重构
   + 重新整理了下项目，之前写的代码太乱，导致后期维护困难。
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

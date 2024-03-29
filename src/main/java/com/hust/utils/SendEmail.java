package com.hust.utils;

import com.hust.dto.UserDTO;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static com.hust.utils.Constants.*;
import static com.hust.utils.Constants.EMAIL_ADDRESS;
import static com.hust.utils.RandomCodeGenerator.generateRandomCode;

public class SendEmail {
    private static final Map<String, Long> lastRequestTimeMap = new ConcurrentHashMap<>();

    public static Result sendEmail(UserDTO userDTO) throws MessagingException, GeneralSecurityException {
        // 加密的话就先拦截一下，处理啊完了再让它进行存储
        String username = userDTO.getUsername();
        Long lastRequestTime = 0L;
        // 从缓存中取出该用户最后一次请求邮件的时间戳
        if (lastRequestTimeMap.get(username) == null) {
            lastRequestTimeMap.put(username, System.currentTimeMillis());
        } else {
            lastRequestTime = lastRequestTimeMap.get(username);
        }
        // 判断当前时间和上一次请求邮件的时间间隔是否超过设定的阈值
        if (lastRequestTime != null && System.currentTimeMillis() - lastRequestTime < 60_000) {
            // 如果未超过阈值，则返回提示信息拒绝发送邮件
            return Result.error(REQUEST_TOO_FREQUENT_MESSAGE);
        }
        // 更新缓存中该用户的时间戳为当前时间
        lastRequestTimeMap.put(username, System.currentTimeMillis());
        Properties prop = new Properties();
        // 设置QQ邮件服务器
        prop.setProperty(MAIL_HOST, "smtp.qq.com");
        // 邮件发送协议
        prop.setProperty(MAIL_TRANSPORT_PROTOCOL, "smtp");
        // 需要验证用户名密码
        prop.setProperty(MAIL_SMTP_AUTH, "true");
        // 关于QQ邮箱，还要设置SSL加密，加上以下代码即可
        MailSSLSocketFactory sf = null;
        sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        // 启用SMTP SSL
        prop.put(MAIL_SMTP_SSL_ENABLE, "true");
        // 设置SSL套接字工厂
        prop.put(MAIL_SMTP_SSL_SOCKET_FACTORY, sf);
        // 使用JavaMail发送邮件的5个步骤
        // 1、创建定义整个应用程序所需的环境信息的Session对象
        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //发件人邮件用户名、授权码
                return new PasswordAuthentication(EMAIL_ADDRESS, EMAIL_PASSWORD);
            }
        });
        // 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);
        // 2、通过session得到transport对象
        Transport ts = null;
        ts = session.getTransport();
        // 3、使用邮箱的用户名和授权码连上邮件服务器
        assert ts != null;
        // 连接到QQ邮件服务器，使用指定的邮箱和密码
        ts.connect(SMTP_SERVER, EMAIL_ADDRESS, EMAIL_PASSWORD);
        // 4、创建邮件：写邮件
        // 注意需要传递Session
        MimeMessage message = new MimeMessage(session);
        // 指明邮件的发件人 24736743
        message.setFrom(new InternetAddress(EMAIL_ADDRESS));
        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(userDTO.getEmail()));
        //邮件的标题
        message.setSubject("以下是注册验证");
        //邮件的文本内容
        message.setContent("<h1 style='color:red'>您的验证码是</h1>" + userDTO.getCode(), "text/html;charset=UTF-8");
        //5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        //6、关闭连接
        ts.close();

        return Result.success();
    }
}

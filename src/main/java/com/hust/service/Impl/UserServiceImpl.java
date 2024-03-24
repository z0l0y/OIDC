package com.hust.service.Impl;

import com.hust.dto.UserDTO;
import com.hust.mapper.UserMapper;
import com.hust.po.UserPO;
import com.hust.service.UserService;
import com.hust.utils.Result;
import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.hust.utils.Constants.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    private final Map<String, Long> lastRequestTimeMap = new ConcurrentHashMap<>();
    /**
     * 采用validator，在正式进入业务逻辑之前先过滤一下非法的userDTO
     */
    private Validator validator;

    @Autowired
    public void UserService(Validator validator) {
        this.validator = validator;
    }

    /**
     * 创建用户.
     *
     * @param userDTO 用户传输对象，包含用户信息
     * @return 创建用户是否成功，true表示创建成功，false表示创建失败
     */
    @Override
    public Result createUser(UserDTO userDTO) throws MessagingException, GeneralSecurityException {
        // 进入业务逻辑之前一定要对我们的数据进行检测，不要污染了数据库
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<UserDTO> violation : violations) {
                System.out.println(violation.getMessage());
            }
            return Result.error(INVALID_DATA_ERROR_MESSAGE);
        }
        // 加密的话就先拦截一下，处理啊完了再让它进行存储
        String username = userDTO.getUsername();
        // 从缓存中取出该用户最后一次请求邮件的时间戳
        Long lastRequestTime = lastRequestTimeMap.get(username);
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
        message.setContent("<h1 style='color:red'>验证成功，您已经成功注册！</h1>", "text/html;charset=UTF-8");
        //5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        //6、关闭连接
        ts.close();
        if (userDTO.getEmail().matches("([a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6})")) {
            // 如果匹配成功的处理逻辑
            String password = userDTO.getPassword();
            MessageDigest md = null;
            md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            // 将字节数组转换为符号表示
            StringBuilder sb = new StringBuilder();
            // 用StringBuilder，避免造成字符池的资源浪费
            for (byte b : messageDigest) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
        }

        UserPO userPO = toUserPO(userDTO);
        int rowsAffected = userMapper.createUser(userPO);
        if (rowsAffected > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    /**
     * 将用户传输对象转换为用户持久化对象.
     *
     * @param userDTO 用户传输对象，包含用户信息
     * @return 转换后的用户持久化对象UserPO
     */
    private UserPO toUserPO(UserDTO userDTO) {
        UserPO userPO = new UserPO();
        userPO.setUsername(userDTO.getUsername());
        userPO.setPassword(userDTO.getPassword());
        userPO.setEmail(userDTO.getEmail());
        userPO.setNickname(userDTO.getNickname());
        userPO.setAvatar(userDTO.getAvatar());
        userPO.setBio(userDTO.getBio());
        return userPO;
    }
}

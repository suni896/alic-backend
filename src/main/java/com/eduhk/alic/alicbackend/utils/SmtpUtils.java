package com.eduhk.alic.alicbackend.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author FuSu
 * @date 2025/1/14 14:53
 */
public class SmtpUtils {

    public static void sendMail(String sourceEmailAddr, String targetEmailAddr, String hostEmailAddr, String verfiCode, String emailPassword) {
        // Session对象:
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", hostEmailAddr); // 设置主机地址
        // smtp.163.com
        // smtp.qq.com
        // smtp.sina.com
        props.setProperty("mail.smtp.auth", "true");// 认证
        // 2.产生一个用于邮件发送的Session对象
        Session session = Session.getInstance(props);

        // Message对象:
        Message message = new MimeMessage(session);
        // 设置发件人：
        try {
            // 4.设置消息的发送者
            Address fromAddr = new InternetAddress(sourceEmailAddr);
            message.setFrom(fromAddr);

            // 5.设置消息的接收者 nkpxcloxbtpxdjai
            Address toAddr = new InternetAddress(targetEmailAddr);
            // TO 直接发送 CC抄送 BCC密送
            message.setRecipient(MimeMessage.RecipientType.TO, toAddr);

            // 6.设置邮件标题
            message.setSubject("Your Verification Code for Accessing Artificial Intelligence and Learning Analytics in Collaboration");
            // 7.设置正文
            message.setContent("Dear User\n\nThis code is"+verfiCode+" \n\n valid for the next 10 minutes. \n\nPlease do not share this code with anyone for security reasons.\n" +
                    "If you did not initiate this request, please disregard this email or contact our support team immediately.\n\nBest regards,\n" +
                    "ALIC Support Team" , "text/html;charset=UTF-8");

            // 8.准备发送，得到火箭
            Transport transport = session.getTransport("smtp");
            // 9.设置火箭的发射目标（第三个参数就是你的邮箱授权码）
            //transport.connect("smtp.163.com", "发送者@163.com", "abcdefghabcdefgh");
            transport.connect(hostEmailAddr, sourceEmailAddr, emailPassword);
            // 10.发送
            transport.sendMessage(message, message.getAllRecipients());

            // Transport对象:
            // Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            //TODO 抛出异常要改一下
        }
    }


}

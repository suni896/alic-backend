package com.eduhk.alic.alicbackend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author FuSu
 * @date 2025/1/14 15:59
 */
public class SmtpConfiguration {

//    @Bean
//    public JavaMailSender getJavaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.qq.com");
//        mailSender.setPort(587);
//        mailSender.setUsername("");
//        mailSender.setPassword("");
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        return mailSender;
//    }
}

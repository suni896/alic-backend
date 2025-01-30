package com.eduhk.alic.alicbackend.service;

import com.eduhk.alic.alicbackend.model.vo.SmtpVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author FuSu
 * @date 2025/1/14 15:26
 */
@Service
@Slf4j
public class SmtpService {

    @Value("${spring.mail.username}")
    private String sourceMailAddr;

    @Resource
    private JavaMailSender javaMailSender;

    private void doSendEmail(String sourceEmailAddr, String targetEmailAddr, String verfiCode){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        //设置发送人
        mailMessage.setFrom(sourceEmailAddr);
        //邮件主题
        mailMessage.setSubject("Your Verification Code for Accessing Artificial Intelligence and Learning Analytics in Collaboration");
        //邮件内容
        //TODO 加样式
        mailMessage.setText("Dear User\n\nThis code is: "+verfiCode+" \n\n valid for the next 10 minutes. \n\nPlease do not share this code with anyone for security reasons.\n" +
                "If you did not initiate this request, please disregard this email or contact our support team immediately.\n\nBest regards,\n" +
                "ALIC Support Team" );
        //收件人
        mailMessage.setTo(targetEmailAddr);
        javaMailSender.send(mailMessage);

    }
    public void sendMail(SmtpVO smtpVO,  String verfiCode) {
        log.info("verfiCode, {}", verfiCode);
        doSendEmail(sourceMailAddr, smtpVO.getUserEmail(), verfiCode);

    }
}

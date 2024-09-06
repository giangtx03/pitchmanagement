package com.pitchmanagement.services.impl;

import com.pitchmanagement.constants.MailConstant;
import com.pitchmanagement.services.SendEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendEmailServiceImpl implements SendEmailService {

    private final JavaMailSender javaMailSender;
    private final Logger logger = LoggerFactory.getLogger(SendEmailServiceImpl.class);
    @Override
    public void sendEmail(String recipient, String subject, String body) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(MailConstant.FROM_EMAIL);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(body, true);

            javaMailSender.send(mimeMessage);
            logger.info("Gửi email thành công tới email : {}", recipient );
        } catch (MessagingException e) {
            logger.warn("Lỗi gửi email : {}", e.getMessage());
            e.printStackTrace();
        }
    }
}

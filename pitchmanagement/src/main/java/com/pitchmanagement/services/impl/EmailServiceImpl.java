package com.pitchmanagement.services.impl;

import com.pitchmanagement.constants.MailConstant;
import com.pitchmanagement.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    @Override
    public void sendEmail(String recipient, String subject, String body) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(MailConstant.FROM_EMAIL);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(body, true);

            javaMailSender.send(mimeMessage);
            log.info("Gửi email thành công tới email : {}", recipient );
        } catch (MessagingException e) {
            log.warn("Lỗi gửi email : {}", e.getMessage());
            e.printStackTrace();
        }
    }
}

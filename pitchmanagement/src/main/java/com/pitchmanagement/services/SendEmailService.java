package com.pitchmanagement.services;

public interface SendEmailService {

    void sendEmail(String recipient, String body, String subject);

}

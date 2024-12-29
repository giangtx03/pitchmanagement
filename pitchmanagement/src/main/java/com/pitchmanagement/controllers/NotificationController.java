package com.pitchmanagement.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public String greeting(String message){
        log.info("gui tin nhan thanh cong: {}", message);
        return message;
    }

}

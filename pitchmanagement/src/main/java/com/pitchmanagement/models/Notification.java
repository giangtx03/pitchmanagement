package com.pitchmanagement.models;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Notification {

    private Long id;
    private Long userId;
    private Long senderId;
    private String title;
    private String message;
    private String type;
    private String targetUrl;
    private boolean isRead;
    private LocalDateTime readAt;
    private LocalDateTime createAt;

}

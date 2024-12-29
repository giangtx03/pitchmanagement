package com.pitchmanagement.models;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Token {

    private String token;
    private Long userId;
    private LocalDateTime expiredTime;

}

package com.pitchmanagement.dtos;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TokenDto {

    private String token;
    private Long userId;
    private LocalDateTime expiredTime;

}

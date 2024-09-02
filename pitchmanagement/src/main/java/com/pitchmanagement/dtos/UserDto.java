package com.pitchmanagement.dtos;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String fullname;
    private String password;
    private String address;
    private String avatar;
    private String phoneNumber;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String role;
    private boolean isActive;
}

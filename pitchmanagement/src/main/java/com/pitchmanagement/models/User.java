package com.pitchmanagement.models;

import com.pitchmanagement.dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
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

    public static User fromUserDto(UserDto userDto){
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .fullname(userDto.getFullname())
                .password(userDto.getPassword())
                .address(userDto.getAddress())
                .avatar(userDto.getAvatar())
                .phoneNumber(userDto.getPhoneNumber())
                .role(userDto.getRole())
                .createAt(userDto.getCreateAt())
                .updateAt(userDto.getUpdateAt())
                .isActive(userDto.isActive())
                .build();
    }
}

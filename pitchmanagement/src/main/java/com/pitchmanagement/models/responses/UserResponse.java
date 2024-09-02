package com.pitchmanagement.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pitchmanagement.dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UserResponse {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("fullname")
    private String fullname;
    @JsonProperty("address")
    private String address;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("create_at")
    private LocalDateTime createAt;
    @JsonProperty("update_at")
    private LocalDateTime updateAt;
    @JsonProperty("role")
    private String role;

    public static UserResponse fromUserDto(UserDto userDto){
        return UserResponse.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .fullname(userDto.getFullname())
                .address(userDto.getAddress())
                .avatar(userDto.getAvatar())
                .phoneNumber(userDto.getPhoneNumber())
                .role(userDto.getRole())
                .createAt(userDto.getCreateAt())
                .updateAt(userDto.getUpdateAt())
                .build();
    }
}

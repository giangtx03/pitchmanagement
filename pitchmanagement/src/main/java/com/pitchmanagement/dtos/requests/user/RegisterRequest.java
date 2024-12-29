package com.pitchmanagement.dtos.requests.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {

    @JsonProperty("email")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @JsonProperty("fullname")
    @NotBlank(message = "Fullname không được rỗng")
    private String fullname;

    @JsonProperty("phone_number")
    @Size(min = 3, message = "Số điện thoại ít nhất 3 chữ số")
    private String phoneNumber;

    @JsonProperty("password")
    @Size(min = 8, message = "Mật khẩu ít nhất chứa 8 ký tự")
    private String password;

}

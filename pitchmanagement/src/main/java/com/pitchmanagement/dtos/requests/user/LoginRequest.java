package com.pitchmanagement.dtos.requests.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {

    @JsonProperty("email")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @JsonProperty("password")
    private String password;
}

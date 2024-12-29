package com.pitchmanagement.dtos.requests.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RenewPassword {

    @JsonProperty("new_password")
    @Size(min = 8, message = "Mật khẩu ít nhất chứa 8 ký tự")
    private String newPassword;

}

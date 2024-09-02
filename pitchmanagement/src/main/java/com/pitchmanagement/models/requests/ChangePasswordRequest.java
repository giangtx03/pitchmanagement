package com.pitchmanagement.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordRequest {

    @JsonProperty("user_id")
    @Min(value = 1, message = "Id người dùng phải lớn hơn 1")
    private Long userId;

    @JsonProperty("old_password")
    private String oldPassword;

    @JsonProperty("new_password")
    @Size(min = 8, message = "Mật khẩu ít nhất chứa 8 ký tự")
    private String newPassword;

}

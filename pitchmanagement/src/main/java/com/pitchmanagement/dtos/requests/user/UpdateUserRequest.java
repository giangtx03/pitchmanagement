package com.pitchmanagement.dtos.requests.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserRequest {

    private Long id;

    @NotBlank(message = "Fullname không được rỗng")
    private String fullname;

    @Size(min = 3, message = "Số điện thoại ít nhất 3 chữ số")
    private String phoneNumber;

    private String address;

    private MultipartFile avatar;

}

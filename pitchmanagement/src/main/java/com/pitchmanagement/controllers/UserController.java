package com.pitchmanagement.controllers;

import com.pitchmanagement.dtos.requests.user.ChangePasswordRequest;
import com.pitchmanagement.dtos.requests.user.UpdateUserRequest;
import com.pitchmanagement.dtos.responses.BaseResponse;
import com.pitchmanagement.dtos.responses.UserResponse;
import com.pitchmanagement.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER', 'ROLE_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @PathVariable("id") Long id
    ) throws Exception {
        UserResponse userResponse = userService.getUserById(id);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .data(userResponse)
                .message("Thông tin người dùng !")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER', 'ROLE_MANAGER')")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUser(
            @ModelAttribute @Valid UpdateUserRequest updateUserRequest
    ) throws Exception {
        UserResponse userResponse = userService.updateUser(updateUserRequest);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .data(userResponse)
                .message("Cập nhật thông tin người dùng thành công!")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER', 'ROLE_MANAGER')")
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody @Valid ChangePasswordRequest changePasswordRequest
    ) throws Exception {
        userService.changePassword(changePasswordRequest);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Thay đổi mật khẩu thành công !")
                .build();
        return ResponseEntity.ok().body(response);
    }
}

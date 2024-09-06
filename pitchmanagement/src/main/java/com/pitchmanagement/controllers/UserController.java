package com.pitchmanagement.controllers;

import com.pitchmanagement.models.requests.ChangePasswordRequest;
import com.pitchmanagement.models.requests.UpdateUserRequest;
import com.pitchmanagement.models.responses.BaseResponse;
import com.pitchmanagement.models.responses.UserResponse;
import com.pitchmanagement.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER', 'ROLE_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @PathVariable("id") Long id
    ){
        try {
            UserResponse userResponse = userService.getUserById(id);

            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .data(userResponse)
                    .message("Thông tin người dùng !")
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(BaseResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER', 'ROLE_MANAGER')")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUser(
            @ModelAttribute @Valid UpdateUserRequest updateUserRequest,
            BindingResult result
    ){
        if (result.hasErrors()) {
            // lấy ra danh sách lỗi
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // trả về danh sách lỗi
            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Lỗi thông tin đầu vào!!!")
                    .data(errorMessages)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
        try {
            UserResponse userResponse = userService.updateUser(updateUserRequest);

            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .data(userResponse)
                    .message("Thông tin người dùng !")
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(BaseResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER', 'ROLE_MANAGER')")
    @PutMapping("/change_password")
    public ResponseEntity<?> changePassword(
            @RequestBody @Valid ChangePasswordRequest changePasswordRequest,
            BindingResult result
    ){
        if (result.hasErrors()) {
            // lấy ra danh sách lỗi
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // trả về danh sách lỗi
            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Lỗi thông tin đầu vào!!!")
                    .data(errorMessages)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
        try {
            userService.changePassword(changePasswordRequest);

            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Thay đổi mật khẩu thành công !")
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(BaseResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }
}

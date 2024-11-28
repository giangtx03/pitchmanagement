package com.pitchmanagement.controllers.publics;

import com.pitchmanagement.models.requests.user.LoginRequest;
import com.pitchmanagement.models.requests.user.RegisterRequest;
import com.pitchmanagement.models.requests.user.RenewPassword;
import com.pitchmanagement.models.responses.BaseResponse;
import com.pitchmanagement.models.responses.LoginResponse;
import com.pitchmanagement.models.responses.RegisterResponse;
import com.pitchmanagement.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("public/${api.prefix}/users")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Valid LoginRequest loginRequest
    ) throws Exception {
        LoginResponse login = userService.login(loginRequest);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .data(login)
                .message("Đăng nhập thành công")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody @Valid RegisterRequest registerRequest
    ) throws Exception {
        RegisterResponse register = userService.register(registerRequest);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(register)
                .message("Đăng ký thành công")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/confirm-email/{token}")
    public ResponseEntity<?> confirmEmail(
            @PathVariable("token") String token
    ) throws Exception {
        userService.confirmEmail(token);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Xác nhận email thành công")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/resend-confirm-email/{email}")
    public ResponseEntity<?> resendConfirmEmail(
            @PathVariable("email") String email
    ) throws Exception {
        userService.resendConfirmEmail(email);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Yêu cầu gửi lại xác nhận email thành công")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/forgot-password/{email}")
    public ResponseEntity<?> forgotPassword(
            @PathVariable("email") String email
    ) throws Exception {
        userService.forgotPassword(email);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Quên mật khẩu thành công")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/renew-password/{token}")
    public ResponseEntity<?> renewPassword(
            @PathVariable("token") String token,
            @RequestBody @Valid RenewPassword renewPassword
    ) throws Exception {
        userService.renewPassword(token, renewPassword);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Làm mới mật khẩu thành công")
                .build();
        return ResponseEntity.ok().body(response);
    }
}

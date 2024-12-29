package com.pitchmanagement.services;

import com.pitchmanagement.dtos.requests.user.*;
import com.pitchmanagement.dtos.responses.LoginResponse;
import com.pitchmanagement.dtos.responses.RegisterResponse;
import com.pitchmanagement.dtos.responses.UserResponse;

public interface UserService {
    LoginResponse login(LoginRequest loginRequest) throws Exception;
    RegisterResponse register(RegisterRequest request) throws Exception;
    UserResponse getUserById(Long id) throws Exception;
    UserResponse updateUser(UpdateUserRequest updateUserRequest) throws Exception;
    void changePassword(ChangePasswordRequest request) throws Exception;
    void confirmEmail(String token) throws Exception;
    void resendConfirmEmail(String email) throws Exception;
    void forgotPassword(String email) throws Exception;
    void renewPassword(String token, RenewPassword renewPassword) throws Exception;
}

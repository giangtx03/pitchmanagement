package com.pitchmanagement.services;

import com.pitchmanagement.models.requests.ChangePasswordRequest;
import com.pitchmanagement.models.requests.LoginRequest;
import com.pitchmanagement.models.requests.RegisterRequest;
import com.pitchmanagement.models.requests.UpdateUserRequest;
import com.pitchmanagement.models.responses.LoginResponse;
import com.pitchmanagement.models.responses.RegisterResponse;
import com.pitchmanagement.models.responses.UserResponse;

public interface UserService {

    LoginResponse login(LoginRequest loginRequest) throws Exception;

    RegisterResponse register(RegisterRequest request) throws Exception;

    UserResponse getUserById(Long id) throws Exception;
    UserResponse updateUser(UpdateUserRequest updateUserRequest) throws Exception;

    void changePassword(ChangePasswordRequest request) throws Exception;
    void confirmEmail(String token) throws Exception;
    void resendConfirmEmail(String email) throws Exception;
}

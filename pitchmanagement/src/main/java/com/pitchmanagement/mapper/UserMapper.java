package com.pitchmanagement.mapper;

import com.pitchmanagement.dtos.requests.user.RegisterRequest;
import com.pitchmanagement.dtos.requests.user.UpdateUserRequest;
import com.pitchmanagement.dtos.responses.LoginResponse;
import com.pitchmanagement.dtos.responses.RegisterResponse;
import com.pitchmanagement.dtos.responses.UserResponse;
import com.pitchmanagement.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "createAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "role", constant = "ROLE_USER")
    @Mapping(target = "isActive", constant = "false")
    User toUser(RegisterRequest request);
    UserResponse toUserResponse(User user);
    LoginResponse toLoginResponse(User user);
    RegisterResponse toRegisterResponse(User user);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "updateAt", expression = "java(java.time.LocalDateTime.now())")
    void updateUser(@MappingTarget User user, UpdateUserRequest request);
}

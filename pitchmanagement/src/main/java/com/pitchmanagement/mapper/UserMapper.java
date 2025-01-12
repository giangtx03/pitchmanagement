package com.pitchmanagement.mapper;

import com.pitchmanagement.dtos.requests.user.RegisterRequest;
import com.pitchmanagement.dtos.requests.user.UpdateUserRequest;
import com.pitchmanagement.dtos.responses.LoginResponse;
import com.pitchmanagement.dtos.responses.UserResponse;
import com.pitchmanagement.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest request);
    UserResponse fromUserToUserResponse(User user);
    LoginResponse fromUserToLoginResponse(User user);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "updateAt", expression = "java(java.time.LocalDateTime.now())")
    void updateUser(@MappingTarget User user, UpdateUserRequest request);
}

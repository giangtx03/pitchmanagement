package com.pitchmanagement.daos;

import com.pitchmanagement.dtos.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface UserDao {
    UserDto getUserByEmail(String email);
    void insert(UserDto userDto);
    void update(UserDto userDto);
    void changePassword(UserDto userDto);
    boolean existingByEmail(@Param("email") String email);
    UserDto getUserById(@Param("id") Long id);
}

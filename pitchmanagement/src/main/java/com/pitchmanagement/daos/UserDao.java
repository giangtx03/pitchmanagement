package com.pitchmanagement.daos;

import com.pitchmanagement.models.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {
    User getUserByEmail(String email);
    void insert(User userDto);
    void update(User userDto);
    void changePassword(User userDto);
    boolean existingByEmail(@Param("email") String email);
    User getUserById(@Param("id") Long id);
}

package com.pitchmanagement.daos;

import com.pitchmanagement.dtos.TokenDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TokenDao {

    void insertToken(TokenDto tokenDto);
    TokenDto getToken(@Param("token") String token);
    void deleteToken(@Param("token") String token);
    void deleteTokenByUserId(@Param("userId") Long userId);

}

package com.pitchmanagement.daos;

import com.pitchmanagement.models.Token;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TokenDao {

    void insertToken(Token tokenDto);
    Token getToken(@Param("token") String token);
    void deleteToken(@Param("token") String token);
    void deleteTokenByUserId(@Param("userId") Long userId);

}

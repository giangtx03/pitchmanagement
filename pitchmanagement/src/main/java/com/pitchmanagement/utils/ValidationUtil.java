package com.pitchmanagement.utils;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.InvalidPropertiesFormatException;

public class ValidationUtil {
    public static void checkActiveUser(Object user) {
        if (user instanceof com.pitchmanagement.models.User) {
            com.pitchmanagement.models.User u = (com.pitchmanagement.models.User) user;
            if (!u.isActive()) {
                throw new BadCredentialsException("Tài khoản chưa active!!!");
            }
        }
    }

    public static void checkValidPassword(String password) throws InvalidPropertiesFormatException {
        if (!password.trim().equals(password)) {
            throw new InvalidPropertiesFormatException("Mật khẩu chứa dấu cách!!!");
        }
    }
}

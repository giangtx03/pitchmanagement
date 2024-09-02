package com.pitchmanagement.securities;

import com.pitchmanagement.models.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@Builder
public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private Collection<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static CustomUserDetails toCustomUser(User user) {
        return CustomUserDetails.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                )
                .build();
    }
}
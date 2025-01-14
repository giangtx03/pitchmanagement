package com.pitchmanagement.services.impl;

import com.pitchmanagement.constants.AppConstant;
import com.pitchmanagement.constants.AuthConstant;
import com.pitchmanagement.constants.MailConstant;
import com.pitchmanagement.daos.TokenDao;
import com.pitchmanagement.daos.UserDao;
import com.pitchmanagement.mapper.UserMapper;
import com.pitchmanagement.models.Token;
import com.pitchmanagement.models.User;
import com.pitchmanagement.dtos.requests.user.*;
import com.pitchmanagement.dtos.responses.LoginResponse;
import com.pitchmanagement.dtos.responses.RegisterResponse;
import com.pitchmanagement.dtos.responses.UserResponse;
import com.pitchmanagement.securities.CustomUserDetails;
import com.pitchmanagement.services.ImageService;
import com.pitchmanagement.services.SendEmailService;
import com.pitchmanagement.services.UserService;
import com.pitchmanagement.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.InvalidPropertiesFormatException;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ImageService imageService;
    private final TokenDao tokenDao;
    private final SendEmailService sendEmailService;
    private final UserMapper userMapper;

    @Value("${api.prefix}")
    private String apiPrefix;
    @Value("${frontend.api}")
    private String frontendApi;
    @Override
    public LoginResponse login(LoginRequest loginRequest) throws Exception {

        User user = userDao.getUserByEmail(loginRequest.getEmail());

        checkValidUser(user);

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Sai thông tin đăng nhập!!!");
        }

        checkActiveUser(user);

        CustomUserDetails customUserDetails = CustomUserDetails.toCustomUser(user);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword(), customUserDetails.getAuthorities()
        );

        authenticationManager.authenticate(authenticationToken);

        String token = jwtUtil.generateToken(user);

        LoginResponse loginResponse = userMapper.toLoginResponse(user);
        loginResponse.setToken(token);
        return loginResponse;
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public RegisterResponse register(RegisterRequest request) throws Exception {

        if(userDao.existingByEmail(request.getEmail())){
            throw new UsernameNotFoundException("Email đã tồn tại!!!");
        }

        checkValidPassword(request.getPassword());

        String passwordEncode = passwordEncoder.encode(request.getPassword());
        request.setPassword(passwordEncode);

        User userDto = userMapper.toUser(request);
        userDao.insert(userDto);

        generateTokenAndSendEmail(userDto, MailConstant.REGISTRATION_CONFIRMATION,
                MailConstant.SUBJECT_TYPE_CONFIRM_EMAIL ,AppConstant.SUB_URL_CONFIRM_EMAIL);

        return userMapper.toRegisterResponse(userDto);
    }

    @Override
    public UserResponse getUserById(Long id) throws Exception {

        User userDto = userDao.getUserById(id);

        checkValidUser(userDto);

        return UserResponse.fromUserDto(userDto);
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public UserResponse updateUser(UpdateUserRequest updateUserRequest) throws Exception {

        User userDto = userDao.getUserById(updateUserRequest.getId());

        checkValidUser(userDto);
        checkActiveUser(userDto);

        String image = "";
        if(updateUserRequest.getAvatar() != null && !updateUserRequest.getAvatar().isEmpty()){
            if(userDto.getAvatar() != null && !userDto.getAvatar().isEmpty()){
                imageService.delete(userDto.getAvatar());
            }
            image = imageService.upload(updateUserRequest.getAvatar());
        }

        userDto.setAvatar(updateUserRequest.getAvatar() != null ? image : userDto.getAvatar());
        userMapper.updateUser(userDto, updateUserRequest);

        userDao.update(userDto);
        return userMapper.toUserResponse(userDto);
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void changePassword(ChangePasswordRequest request) throws Exception {
        User user = userDao.getUserById(request.getUserId());

        checkValidUser(user);
        checkValidPassword(request.getNewPassword());

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            throw new BadCredentialsException("Mật khẩu không chính xác!!!");
        }

        User userDto = User.builder()
                .id(request.getUserId())
                .password(passwordEncoder.encode(request.getNewPassword()))
                .updateAt(LocalDateTime.now())
                .build();
        userDao.changePassword(userDto);
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void confirmEmail(String token) throws Exception {
        Token tokenDto = tokenDao.getToken(token);
        if(tokenDto == null){
            throw new NotFoundException("Mã xác nhận không hợp lệ!");
        }

        if(tokenDto.getExpiredTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Mã xác nhận đã hết hạn");
        }

        User userDto = userDao.getUserById(tokenDto.getUserId());
        if(userDto == null){
            throw new UsernameNotFoundException("Người dùng không tồn tại!!!");
        }

        userDto.setActive(true);
        userDto.setUpdateAt(LocalDateTime.now());

        userDao.update(userDto);
        tokenDao.deleteToken(token);
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void resendConfirmEmail(String email) throws Exception {
        User userDto = userDao.getUserByEmail(email);

        checkValidUser(userDto);

        tokenDao.deleteTokenByUserId(userDto.getId());
        generateTokenAndSendEmail(userDto, MailConstant.REGISTRATION_CONFIRMATION,
                MailConstant.SUBJECT_TYPE_CONFIRM_EMAIL, AppConstant.SUB_URL_CONFIRM_EMAIL);
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void forgotPassword(String email) throws Exception {
        User userDto = userDao.getUserByEmail(email);

        checkValidUser(userDto);
        checkActiveUser(userDto);

        tokenDao.deleteTokenByUserId(userDto.getId());
        generateTokenAndSendEmail(userDto, MailConstant.FORGOT_PASSWORD,
                MailConstant.SUBJECT_TYPE_FORGOT_PASSWORD, AppConstant.SUB_URL_RENEW_PASSWORD);
    }

    @Override
    public void renewPassword(String token, RenewPassword renewPassword) throws Exception {
        Token tokenDto = Optional.ofNullable(tokenDao.getToken(token))
                .orElseThrow(() -> new NotFoundException("Mã xác nhận không hợp lệ"));

        if(tokenDto.getExpiredTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Mã xác nhận đã hết hạn");
        }

        checkValidPassword(renewPassword.getNewPassword());

        User userDto = userDao.getUserById(tokenDto.getUserId());
        checkValidUser(userDto);

        userDto.setPassword(passwordEncoder.encode(renewPassword.getNewPassword()));
        userDto.setUpdateAt(LocalDateTime.now());

        userDao.changePassword(userDto);
        tokenDao.deleteToken(token);
    }

    private void generateTokenAndSendEmail(User userDto, String formBody, String subjectType, String subUrl){
        Token tokenDto = Token.builder()
                .token(UUID.randomUUID().toString())
                .userId(userDto.getId())
                .expiredTime(LocalDateTime.now().plusMinutes(15))
                .build();
        tokenDao.insertToken(tokenDto);

        String body = formBody
                .replace("{username}", userDto.getFullname())
                .replace("{confirmationLink}", frontendApi + subUrl + tokenDto.getToken());

        String subject = subjectType + " cho người dùng " + userDto.getFullname();

//        sendEmailService.sendEmail(userDto.getEmail(), subject, body);
    }


    private void checkValidUser(User user) {
        if(user == null){
            throw new UsernameNotFoundException("Người dùng không tồn tại!!!");
        }
    }

    private void checkActiveUser(User user) {
        if(!user.isActive()){
            throw new BadCredentialsException("Tài khoản chưa active!!!");
        }
    }

    private void checkValidPassword(String password) throws InvalidPropertiesFormatException {
        if(!password.trim().equals(password)){
            throw new InvalidPropertiesFormatException("Mật khẩu chứa dấu cách!!!");
        }
    }
}

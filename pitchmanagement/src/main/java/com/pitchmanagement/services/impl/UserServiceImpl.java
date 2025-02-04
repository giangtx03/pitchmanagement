package com.pitchmanagement.services.impl;

import com.pitchmanagement.constants.AppConstant;
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
import com.pitchmanagement.services.ImageService;
import com.pitchmanagement.services.EmailService;
import com.pitchmanagement.services.UserService;
import com.pitchmanagement.utils.JwtUtil;
import com.pitchmanagement.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final EmailService emailService;
    private final UserMapper userMapper;

    @Value("${frontend.api}")
    private String frontendApi;
    @Override
    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        User user = Optional.ofNullable(userDao.getUserByEmail(loginRequest.getEmail()))
                .orElseThrow(() -> new UsernameNotFoundException("Người dùng không tồn tại!!!"));

        ValidationUtil.checkActiveUser(user);

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Sai thông tin đăng nhập!!!");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));

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

        ValidationUtil.checkValidPassword(request.getPassword());

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = userMapper.toUser(request);
        userDao.insert(user);

        sendConfirmationEmail(user, AppConstant.SUB_URL_CONFIRM_EMAIL);
        return userMapper.toRegisterResponse(user);
    }

    @Override
    public UserResponse getUserById(Long id) throws Exception {
        User user = Optional.ofNullable(userDao.getUserById(id))
                .orElseThrow(() -> new UsernameNotFoundException("Người dùng không tồn tại!!!"));
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public UserResponse updateUser(UpdateUserRequest request) throws Exception {
        User user = Optional.ofNullable(userDao.getUserById(request.getId()))
                .orElseThrow(() -> new UsernameNotFoundException("Người dùng không tồn tại!!!"));

        ValidationUtil.checkActiveUser(user);

        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            imageService.delete(user.getAvatar());
            user.setAvatar(imageService.upload(request.getAvatar()));
        }

        userMapper.updateUser(user, request);
        userDao.update(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void changePassword(ChangePasswordRequest request) throws Exception {
        User user = Optional.ofNullable(userDao.getUserById(request.getUserId()))
                .orElseThrow(() -> new UsernameNotFoundException("Người dùng không tồn tại!!!"));

        ValidationUtil.checkValidPassword(request.getNewPassword());
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Mật khẩu không chính xác!!!");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdateAt(LocalDateTime.now());
        userDao.changePassword(user);
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
        User user = Optional.ofNullable(userDao.getUserByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("Người dùng không tồn tại!!!"));

        tokenDao.deleteTokenByUserId(user.getId());
        sendConfirmationEmail(user, AppConstant.SUB_URL_CONFIRM_EMAIL);
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void forgotPassword(String email) throws Exception {
        User user = Optional.ofNullable(userDao.getUserByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("Người dùng không tồn tại!!!"));

        ValidationUtil.checkActiveUser(user);

        tokenDao.deleteTokenByUserId(user.getId());
        sendResetPasswordEmail(user, AppConstant.SUB_URL_RENEW_PASSWORD);
    }

    @Override
    public void renewPassword(String token, RenewPassword renewPassword) throws Exception {
        Token tokenDto = Optional.ofNullable(tokenDao.getToken(token))
                .orElseThrow(() -> new NotFoundException("Mã xác nhận không hợp lệ"));

        if(tokenDto.getExpiredTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Mã xác nhận đã hết hạn");
        }

        ValidationUtil.checkValidPassword(renewPassword.getNewPassword());

        User user = Optional.ofNullable(userDao.getUserById(tokenDto.getUserId()))
                .orElseThrow(() -> new UsernameNotFoundException("Người dùng không tồn tại!!!"));

        user.setPassword(passwordEncoder.encode(renewPassword.getNewPassword()));
        user.setUpdateAt(LocalDateTime.now());

        userDao.changePassword(user);
        tokenDao.deleteToken(token);
    }


    private void sendConfirmationEmail(User user, String subUrl) {
        Token token = new Token(UUID.randomUUID().toString(), user.getId(), LocalDateTime.now().plusMinutes(15));
        tokenDao.insertToken(token);

        String body = String.format(MailConstant.REGISTRATION_CONFIRMATION, user.getFullname(), frontendApi + subUrl + token.getToken());
        String subject = MailConstant.SUBJECT_TYPE_CONFIRM_EMAIL + " cho người dùng " + user.getFullname();
        emailService.sendEmail(user.getEmail(), subject, body);
    }

    private void sendResetPasswordEmail(User user, String subUrl) {
        Token token = new Token(UUID.randomUUID().toString(), user.getId(), LocalDateTime.now().plusMinutes(15));
        tokenDao.insertToken(token);

        String body = String.format(MailConstant.FORGOT_PASSWORD, user.getFullname(), frontendApi + subUrl + token.getToken());
        String subject = MailConstant.SUBJECT_TYPE_FORGOT_PASSWORD + " cho người dùng " + user.getFullname();
        emailService.sendEmail(user.getEmail(), subject, body);
    }
}

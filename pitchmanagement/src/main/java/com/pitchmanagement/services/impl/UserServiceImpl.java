package com.pitchmanagement.services.impl;

import com.pitchmanagement.constants.AppConstant;
import com.pitchmanagement.constants.AuthConstant;
import com.pitchmanagement.constants.MailConstant;
import com.pitchmanagement.daos.TokenDao;
import com.pitchmanagement.daos.UserDao;
import com.pitchmanagement.dtos.TokenDto;
import com.pitchmanagement.dtos.UserDto;
import com.pitchmanagement.models.User;
import com.pitchmanagement.models.requests.ChangePasswordRequest;
import com.pitchmanagement.models.requests.LoginRequest;
import com.pitchmanagement.models.requests.RegisterRequest;
import com.pitchmanagement.models.requests.UpdateUserRequest;
import com.pitchmanagement.models.responses.LoginResponse;
import com.pitchmanagement.models.responses.RegisterResponse;
import com.pitchmanagement.models.responses.UserResponse;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
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

    @Value("${api.prefix}")
    private String apiPrefix;
    @Override
    public LoginResponse login(LoginRequest loginRequest) throws Exception {

        UserDto userDto = userDao.getUserByEmail(loginRequest.getEmail());

        if(userDto == null){
            throw new UsernameNotFoundException("Sai thông tin đăng nhập!!!");
        }

        User user = User.fromUserDto(userDto);

        if(!user.isActive()){
            throw new BadCredentialsException("Tài khoản chưa active!!!");
        }

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Sai thông tin đăng nhập!!!");
        }

        CustomUserDetails customUserDetails = CustomUserDetails.toCustomUser(user);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword(), customUserDetails.getAuthorities()
        );

        authenticationManager.authenticate(authenticationToken);

        String token = jwtUtil.generateToken(user);
        return LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullname(user.getFullname())
                .avatar(user.getAvatar())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .token(token)
                .createAt(user.getCreateAt())
                .updateAt(user.getUpdateAt())
                .role(user.getRole())
                .build();
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public RegisterResponse register(RegisterRequest request) throws Exception {

        if(userDao.existingByEmail(request.getEmail())){
            throw new UsernameNotFoundException("Email đã tồn tại!!!");
        }

        if(!request.getPassword().trim().equals(request.getPassword())){
            throw new InvalidPropertiesFormatException("Mật khẩu chứa dấu cách ở đầu và cuối!!!");
        }

        UserDto userDto = UserDto.builder()
                .email(request.getEmail())
                .fullname(request.getFullname())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .role(AuthConstant.ROLE_USER)
                .isActive(false)
                .build();
        userDao.insert(userDto);

        generateTokenAndSendEmail(userDto);

        return RegisterResponse.builder()
                .id(userDto.getId())
                .fullname(userDto.getFullname())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .role(userDto.getRole())
                .createAt(userDto.getCreateAt())
                .updateAt(userDto.getUpdateAt())
                .build();
    }

    @Override
    public UserResponse getUserById(Long id) throws Exception {

        UserDto userDto = userDao.getUserById(id);

        if(userDto == null){
            throw new UsernameNotFoundException("Người dùng không tồn tại!!!");
        }

        return UserResponse.fromUserDto(userDto);
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public UserResponse updateUser(UpdateUserRequest updateUserRequest) throws Exception {

        UserDto userFromDb = userDao.getUserById(updateUserRequest.getId());

        if(userFromDb == null){
            throw new UsernameNotFoundException("Người dùng không tồn tại!!!");
        }

        String image = "";
        if(updateUserRequest.getAvatar() != null && !updateUserRequest.getAvatar().isEmpty()){
            if(userFromDb.getAvatar() != null && !userFromDb.getAvatar().isEmpty()){
                imageService.delete(userFromDb.getAvatar());
            }
            image = imageService.upload(updateUserRequest.getAvatar());
        }

        UserDto userDto = UserDto.builder()
                .id(updateUserRequest.getId())
                .address(updateUserRequest.getAddress() != null ? updateUserRequest.getAddress() : userFromDb.getAddress())
                .fullname(updateUserRequest.getFullname() != null ? updateUserRequest.getFullname() : userFromDb.getFullname())
                .avatar(updateUserRequest.getAvatar() != null ? image : userFromDb.getAvatar())
                .phoneNumber(updateUserRequest.getPhoneNumber() != null ? updateUserRequest.getPhoneNumber() : userFromDb.getPhoneNumber())
                .updateAt(LocalDateTime.now())
                .build();
        userDao.update(userDto);
        return UserResponse.fromUserDto(userDto);
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void changePassword(ChangePasswordRequest request) throws Exception {
        UserDto userFromDb = userDao.getUserById(request.getUserId());

        if(userFromDb == null){
            throw new UsernameNotFoundException("Người dùng không tồn tại!!!");
        }

        if(!request.getNewPassword().trim().equals(request.getNewPassword())){
            throw new InvalidPropertiesFormatException("Mật khẩu chứa dấu cách ở đầu và cuối!!!");
        }

        User user = User.fromUserDto(userFromDb);

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            throw new BadCredentialsException("Mật khẩu không chính xác!!!");
        }

        UserDto userDto = UserDto.builder()
                .id(request.getUserId())
                .password(passwordEncoder.encode(request.getNewPassword()))
                .updateAt(LocalDateTime.now())
                .build();
        userDao.changePassword(userDto);
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void confirmEmail(String token) throws Exception {
        TokenDto tokenDto = tokenDao.getToken(token);
        if(tokenDto == null){
            throw new NotFoundException("Mã xác nhận không hợp lệ!");
        }

        if(tokenDto.getExpiredTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Mã xác nhận đã hết hạn");
        }

        UserDto userDto = userDao.getUserById(tokenDto.getUserId());
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
        UserDto userDto = Optional.ofNullable(userDao.getUserByEmail(email))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng!"));

        tokenDao.deleteTokenByUserId(userDto.getId());
        generateTokenAndSendEmail(userDto);
    }

    private void generateTokenAndSendEmail(UserDto userDto){
        TokenDto tokenDto = TokenDto.builder()
                .token(UUID.randomUUID().toString())
                .userId(userDto.getId())
                .expiredTime(LocalDateTime.now().plusMinutes(15))
                .build();
        tokenDao.insertToken(tokenDto);

        String body = MailConstant.REGISTRATION_CONFIRMATION
                .replace("{username}", userDto.getFullname())
                .replace("{confirmationLink}", AppConstant.APP_API_URL + "public/" + apiPrefix + "/users/confirm-email/" + tokenDto.getToken());

        String subject = String.format("Xác thực email cho tài khoản %s", userDto.getFullname());

        sendEmailService.sendEmail(userDto.getEmail(), subject, body);
    }

}

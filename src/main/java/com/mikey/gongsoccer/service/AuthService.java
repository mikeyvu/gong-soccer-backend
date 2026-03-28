package com.mikey.gongsoccer.service;

import com.mikey.gongsoccer.dto.request.LoginUserDto;
import com.mikey.gongsoccer.dto.request.SignupUserDto;
import com.mikey.gongsoccer.dto.request.VerifyUserDto;
import com.mikey.gongsoccer.dto.response.SignupResponse;
import com.mikey.gongsoccer.entity.user.UserEntity;

public interface AuthService {
    SignupResponse signup(SignupUserDto request);
    public UserEntity authenticate(LoginUserDto input);
    public void verifyUser(VerifyUserDto input);
    public void resendVerificationCode(String email);
    public void sendVerificationEmail(UserEntity user);
}

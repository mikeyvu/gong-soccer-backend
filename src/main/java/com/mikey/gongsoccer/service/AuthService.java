package com.mikey.gongsoccer.service;

import com.mikey.gongsoccer.dto.request.SignupRequest;
import com.mikey.gongsoccer.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse signup(SignupRequest request);
}

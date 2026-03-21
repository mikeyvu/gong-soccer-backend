package com.mikey.gongsoccer.controller;

import com.mikey.gongsoccer.dto.request.SignupRequest;
import com.mikey.gongsoccer.dto.response.AuthResponse;
import com.mikey.gongsoccer.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @RequestBody SignupRequest request
            ) {
        return ResponseEntity.ok(authService.signup(request));
    }
}

package com.mikey.gongsoccer.service.impl;

import com.mikey.gongsoccer.dto.request.LoginUserDto;
import com.mikey.gongsoccer.dto.request.SignupUserDto;
import com.mikey.gongsoccer.dto.request.VerifyUserDto;
import com.mikey.gongsoccer.dto.response.SignupResponse;
import com.mikey.gongsoccer.entity.team.TeamEntity;
import com.mikey.gongsoccer.entity.user.RoleEntity;
import com.mikey.gongsoccer.entity.user.UserEntity;
import com.mikey.gongsoccer.repository.RoleRepository;
import com.mikey.gongsoccer.repository.TeamRepository;
import com.mikey.gongsoccer.repository.UserRepository;
import com.mikey.gongsoccer.service.AuthService;
import com.mikey.gongsoccer.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TeamRepository teamRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Override
    public SignupResponse signup(SignupUserDto request) {
        //Check if email already exist
        if (userRepository.findUserEntitiesByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        //Get player role and team they belong to
        RoleEntity playerRole = roleRepository.findRoleEntitiesByName("PLAYER")
                .orElseThrow(() -> new RuntimeException("PLAYER role not found"));
        TeamEntity team1 = teamRepository.findTeamEntitiesById(1L)
                .orElseThrow(() -> new RuntimeException("Team 1 not found"));
        //Create new User
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setBalance(BigDecimal.ZERO);
        user.setEnabled(false);
        user.setRoles(Set.of(playerRole));
        user.setTeamEntity(team1);

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiredAt(LocalDateTime.now().plusMinutes(10));
        //send verification code to user email
        sendVerificationEmail(user);
        //Save user
        userRepository.save(user);
        return new SignupResponse("User registered successfully");
    }

    @Override
    public UserEntity authenticate(LoginUserDto input) {
        UserEntity user = userRepository.
                findUserEntitiesByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified. Please verify your account.");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                input.getEmail(),
                input.getPassword()
        ));
        return user;
     }

    @Override
     public void verifyUser(VerifyUserDto input) {
        Optional<UserEntity> optionalUser = userRepository.findUserEntitiesByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            if (user.getVerificationCodeExpiredAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiredAt(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public void resendVerificationCode(String email) {
        Optional<UserEntity> optionalUser = userRepository.findUserEntitiesByEmail(email);

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }

            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiredAt(LocalDateTime.now().plusMinutes(10));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public void sendVerificationEmail(UserEntity user) {
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) * 100000;
        return String.valueOf(code);
    }


}

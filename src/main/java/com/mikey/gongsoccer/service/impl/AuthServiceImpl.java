package com.mikey.gongsoccer.service.impl;

import com.mikey.gongsoccer.dto.request.SignupRequest;
import com.mikey.gongsoccer.dto.response.AuthResponse;
import com.mikey.gongsoccer.entity.team.TeamEntity;
import com.mikey.gongsoccer.entity.user.RoleEntity;
import com.mikey.gongsoccer.entity.user.UserEntity;
import com.mikey.gongsoccer.repository.RoleRepository;
import com.mikey.gongsoccer.repository.TeamRepository;
import com.mikey.gongsoccer.repository.UserRepository;
import com.mikey.gongsoccer.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TeamRepository teamRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public AuthResponse signup(SignupRequest request) {

        //Check if email already exist
        if (userRepository.findUserEntitiesByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        //Get player role
        RoleEntity playerRole = roleRepository.findRoleEntitiesByName("PLAYER")
                .orElseThrow(() -> new RuntimeException("PLAYER role not found"));
        TeamEntity team1 = teamRepository.findTeamEntitiesById(1L)
                .orElseThrow(() -> new RuntimeException("Team 1 not found"));

        //Create new User
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setBalance(BigDecimal.ZERO);
        user.setRoles(Set.of(playerRole));
        user.setTeamEntity(team1);

        //Save user
        userRepository.save(user);


        return new AuthResponse("User registered successfully");
    }
}

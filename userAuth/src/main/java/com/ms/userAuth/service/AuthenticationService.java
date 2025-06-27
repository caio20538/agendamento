package com.ms.userAuth.service;

import com.ms.userAuth.controller.dto.request.LoginRequest;
import com.ms.userAuth.model.UserEntity;
import com.ms.userAuth.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity authenticate(LoginRequest loginRequest) {
        return userRepository.findByEmail(loginRequest.email())
                .filter(user -> user.isLoginCorrect(loginRequest, passwordEncoder))
                .orElseThrow(() -> new BadCredentialsException("User or Password is invalid!"));
    }
}
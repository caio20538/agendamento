package com.ms.userAuth.service;

import com.ms.userAuth.controller.dto.request.UserRequest;
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

    public UserEntity authenticate(UserRequest userRequest) {
        return userRepository.findByEmail(userRequest.email())
                .filter(user -> user.isLoginCorrect(userRequest, passwordEncoder))
                .orElseThrow(() -> new BadCredentialsException("User or Password is invalid!"));
    }
}
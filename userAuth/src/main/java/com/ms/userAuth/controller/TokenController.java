package com.ms.userAuth.controller;

import com.ms.userAuth.controller.dto.request.UserRequest;
import com.ms.userAuth.controller.dto.response.UserResponse;
import com.ms.userAuth.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody @Valid UserRequest userRequest){
        return tokenService.tokenGenerator(userRequest);
    }
}

package com.ms.userAuth.controller;

import com.ms.userAuth.controller.dto.request.LoginRequest;
import com.ms.userAuth.controller.dto.response.LoginResponse;
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
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest){
        return tokenService.tokenGenerator(loginRequest);
    }

    //implementar os logs
}

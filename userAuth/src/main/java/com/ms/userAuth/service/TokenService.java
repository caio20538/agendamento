package com.ms.userAuth.service;

import com.ms.userAuth.controller.dto.request.UserRequest;
import com.ms.userAuth.controller.dto.response.UserResponse;
import com.ms.userAuth.model.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final AuthenticationService authenticationService;

    public TokenService(JwtEncoder jwtEncoder, AuthenticationService authenticationService) {
        this.jwtEncoder = jwtEncoder;
        this.authenticationService = authenticationService;
    }

    public ResponseEntity<UserResponse> tokenGenerator(UserRequest userRequest) {
        UserEntity user = authenticationService.authenticate(userRequest);

        var now = Instant.now();
        var expiresIn = 300L;

        var scopes = user.getRoles().stream()
                .map(role -> role.getName().name())  // Converte enum para String
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new UserResponse(jwtValue, expiresIn));
    }
}

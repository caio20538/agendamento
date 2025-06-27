package com.ms.userAuth.controller.dto.response;

public record LoginResponse(String accessToken, Long expiresIn) {
}

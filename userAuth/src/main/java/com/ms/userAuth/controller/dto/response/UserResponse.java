package com.ms.userAuth.controller.dto.response;

public record UserResponse(String accessToken, Long expiresIn) {
}

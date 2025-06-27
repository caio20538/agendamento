package com.ms.userAuth.controller.dto;


import com.ms.userAuth.model.UserEntity;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserResponseDTO(UUID id, String email, Set<String> roles) {

    public static UserResponseDTO fromEntity(UserEntity user) {
        var roleNames = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
        return new UserResponseDTO(user.getUserId(), user.getEmail(), roleNames);
    }
}

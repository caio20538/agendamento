package com.ms.userAuth.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDTO (@NotBlank @Email String email,
                             @NotBlank String password){
}

package com.ms.userAuth.service;

import com.ms.userAuth.controller.dto.CreateUserDTO;
import com.ms.userAuth.controller.dto.UserResponseDTO;
import com.ms.userAuth.model.UserEntity;
import com.ms.userAuth.model.enums.RoleEnum;
import com.ms.userAuth.repository.RoleRepository;
import com.ms.userAuth.repository.UserRepository;
import com.ms.userAuth.util.mappers.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public ResponseEntity<Void> createUser(CreateUserDTO userDTO){
        validateUserExist(userDTO.email());

        var customer = roleRepository.findByName(RoleEnum.CUSTOMER);

        UserEntity user = userMapper.dtoToEntity(userDTO, customer);

        return ResponseEntity
                .created(URI.create("/users/" + user.getUserId()))
                .build();
    }

    public List<UserResponseDTO> listAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::fromEntity)
                .toList();
    }


    private void validateUserExist(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already exists.");
        });
    }

    public ResponseEntity<Void> deleteOwnAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String subject = auth.getName(); // o sub do JWT

        UUID userId;
        try {
            userId = UUID.fromString(subject);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid UUID in token");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> deleteUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }


}

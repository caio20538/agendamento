package com.ms.userAuth.service;

import com.ms.userAuth.controller.dto.CreateUserDTO;
import com.ms.userAuth.controller.dto.request.UserUpdateRequest;
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
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserValidationService userValidationService;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository, UserMapper userMapper, UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.userValidationService = userValidationService;
    }

    @Transactional
    public ResponseEntity<Void> createUser(CreateUserDTO userDTO){
        userValidationService.validateUserExist(userDTO.email());

        var customer = roleRepository.findByName(RoleEnum.CUSTOMER);

        UserEntity user = userMapper.dtoToEntity(userDTO, customer);

        userRepository.save(user);

        return ResponseEntity
                .created(URI.create("/users/" + user.getUserId()))
                .build();
    }

    public ResponseEntity<Void> deleteOwnAccount() {
        UUID userId = getAuthenticatedUser();

        IsUserNotExist(!userRepository.existsById(userId));

        userRepository.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    private void IsUserNotExist(boolean userRepositoryNotExist) {
        if (userRepositoryNotExist) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
    }

    private UUID getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String subject = auth.getName(); // o sub do JWT

        UUID userId;
        try {
            return userId = UUID.fromString(subject);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid UUID in token");
        }

    }

    @Transactional
    public ResponseEntity<Void> updateOwnAccount(UserUpdateRequest request){
        UUID userId = getAuthenticatedUser();

        int rowsUpdated = userRepository.updateUserInfo(userId, request.email(), request.password());

        IsUserNotExist(rowsUpdated == 0);

        return ResponseEntity.noContent().build();
    }






}

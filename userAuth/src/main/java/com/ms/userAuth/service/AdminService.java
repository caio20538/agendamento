package com.ms.userAuth.service;

import com.ms.userAuth.controller.dto.CreateUserDTO;
import com.ms.userAuth.controller.dto.request.EmailRequest;
import com.ms.userAuth.controller.dto.request.UserUpdateRequest;
import com.ms.userAuth.controller.dto.response.UserResponseDTO;
import com.ms.userAuth.model.UserEntity;
import com.ms.userAuth.model.enums.RoleEnum;
import com.ms.userAuth.repository.RoleRepository;
import com.ms.userAuth.repository.UserRepository;
import com.ms.userAuth.util.mappers.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserValidationService userValidationService;


    public AdminService(UserRepository userRepository,
                        RoleRepository roleRepository, UserMapper userMapper, UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.userValidationService = userValidationService;
    }

    @Transactional
    public ResponseEntity<Void> createUserAdm(CreateUserDTO userDTO){
        userValidationService.validateUserExist(userDTO.email());

        var customer = roleRepository.findByName(RoleEnum.ADMIN);

        UserEntity user = userMapper.dtoToEntity(userDTO, customer);

        userRepository.save(user);

        return ResponseEntity
                .created(URI.create("/users/" + user.getUserId()))
                .build();
    }

    @Transactional
    public ResponseEntity<Void> deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<Void> updateUserByEmail(String oldEmail, UserUpdateRequest request) {

        userRepository.updateUserInfoByEmail(oldEmail, request.email(), request.password());
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Optional<UserEntity>> userFindByEmail(EmailRequest email){
        //TODO: criar um dto
        return ResponseEntity.ok(userRepository.findByEmail(email.email()));
    }

    public List<UserResponseDTO> listAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::fromEntity)
                .toList();
    }
}

package com.ms.userAuth.service;

import com.ms.userAuth.controller.dto.CreateUserDTO;
import com.ms.userAuth.controller.dto.UserResponseDTO;
import com.ms.userAuth.model.UserEntity;
import com.ms.userAuth.model.enums.RoleEnum;
import com.ms.userAuth.repository.RoleRepository;
import com.ms.userAuth.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public ResponseEntity<Void> createUser(CreateUserDTO userDTO){
        validateUserDoesNotExist(userDTO.email());

        var customer = roleRepository.findByName(RoleEnum.CUSTOMER);

        var user = new UserEntity();
        user.setEmail(userDTO.email());
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setRoles(Set.of(customer));
        userRepository.save(user);

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


    private void validateUserDoesNotExist(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already exists.");
        });
    }
}

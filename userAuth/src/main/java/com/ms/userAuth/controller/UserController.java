package com.ms.userAuth.controller;

import com.ms.userAuth.controller.dto.CreateUserDTO;
import com.ms.userAuth.controller.dto.UserResponseDTO;
import com.ms.userAuth.controller.dto.request.UserRequest;
import com.ms.userAuth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> newUser(@RequestBody @Valid CreateUserDTO userDTO){
        return userService.createUser(userDTO);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> listUsers() {
        var users = userService.listAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteOwnAccount() {
        return userService.deleteOwnAccount();
    }

    @DeleteMapping("/admin/{email}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        return userService.deleteUserByEmail(email);
    }

    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody @Valid UserRequest userDTO){
        //TODO: atualizar o usuário, proprietário ou adm
        return ResponseEntity.ok().build();
    }

}

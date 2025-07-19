package com.ms.userAuth.controller;

import com.ms.userAuth.controller.dto.CreateUserDTO;
import com.ms.userAuth.controller.dto.request.EmailRequest;
import com.ms.userAuth.controller.dto.response.UserResponseDTO;
import com.ms.userAuth.controller.dto.request.UserUpdateRequest;
import com.ms.userAuth.model.UserEntity;
import com.ms.userAuth.service.AdminService;
import com.ms.userAuth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(UserService userService, AdminService adminService) {

        this.adminService = adminService;
    }

    @PostMapping()
    public ResponseEntity<Void> newUserAdm(@RequestBody @Valid CreateUserDTO userDTO){
        return adminService.createUserAdm(userDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listUsers() {
        var users = adminService.listAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/find")
    public ResponseEntity<Optional<UserEntity>> findUser(@RequestBody EmailRequest email){
        return adminService.userFindByEmail(email);
    }

    @DeleteMapping("delete/{email}")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        return adminService.deleteUserByEmail(email);
    }

    @PutMapping("update/{email}")
    public ResponseEntity<Void> updateByEmail(@PathVariable String email, @RequestBody UserUpdateRequest request) {
        return adminService.updateUserByEmail(email, request);
    }
}

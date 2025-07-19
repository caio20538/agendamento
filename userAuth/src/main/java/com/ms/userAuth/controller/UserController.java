package com.ms.userAuth.controller;

import com.ms.userAuth.controller.dto.CreateUserDTO;
import com.ms.userAuth.controller.dto.request.UserUpdateRequest;
import com.ms.userAuth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<Void> newUser(@RequestBody @Valid CreateUserDTO userDTO){
        return userService.createUser(userDTO);
    }

    @DeleteMapping("/deleting")
    public ResponseEntity<Void> deleteOwnAccount() {
        return userService.deleteOwnAccount();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateOwnAccount(@RequestBody UserUpdateRequest request) {
        return userService.updateOwnAccount(request);
    }

}

package com.hackathon.KDT_HACK.Registration;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/secured")
public class MainController {

    private UserRepository userRepository;
    private UserService userService;

    public MainController(UserRepository userRepository, UserService userService) {

        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('USER') and #id == authentication.principal.id")
    public User userAccess(@PathVariable("id") Long id){


        return  userRepository.findUserById(id);

    }
    @PostMapping("/user/{id}")
    @PreAuthorize("hasRole('USER') and #id == authentication.principal.id")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UpdateUserRequest updateUserRequest){



        return ResponseEntity.ok().body(userService.updateUser(updateUserRequest, id));
    }

    @PostMapping("/user/{id}/change-password")
    @PreAuthorize("hasRole('USER') and #id == authentication.principal.id")
    public ResponseEntity<?> changePassword(@PathVariable Long id,
                                            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.ok("Password changed successfully");
    }


}

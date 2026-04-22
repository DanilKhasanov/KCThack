package com.hackathon.KDT_HACK.controller;


import com.hackathon.KDT_HACK.dto.ChangePasswordRequest;
import com.hackathon.KDT_HACK.dto.UpdateUserRequest;
import com.hackathon.KDT_HACK.entity.User;
import com.hackathon.KDT_HACK.repository.UserRepository;
import com.hackathon.KDT_HACK.service.UserDetailsImpl;
import com.hackathon.KDT_HACK.service.UserService;
import com.hackathon.KDT_HACK.dto.ResponseDTO;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
public class MainController {

    private UserRepository userRepository;
    private UserService userService;

    public MainController(UserRepository userRepository, UserService userService) {

        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public User userAccess(Authentication auth){

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return userService.findUserWithDetails(userDetails.getId());

    }
    @PostMapping("/user/{id}")
    @PreAuthorize("hasRole('USER') and #id == authentication.principal.id")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @Valid @RequestBody UpdateUserRequest updateUserRequest){



        return ResponseEntity.ok().body(userService.updateUser(updateUserRequest, id));
    }

    @PostMapping("/user/{id}/change-password")
    @PreAuthorize("hasRole('USER') and #id == authentication.principal.id")
    public ResponseEntity<?> changePassword(@PathVariable String id,
                                            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.ok(new ResponseDTO("Password changed successfully"));
    }


}

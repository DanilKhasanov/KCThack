package com.hackathon.KDT_HACK.Registration;


import com.hackathon.KDT_HACK.PersonalAccount.ChangePasswordRequest;
import com.hackathon.KDT_HACK.PersonalAccount.UpdateUserRequest;
import com.hackathon.KDT_HACK.Response.ResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public User userAccess(@PathVariable("id") String id){

        return  userService.findUser(id);

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

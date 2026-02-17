package com.hackathon.KDT_HACK.Registration;


import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secured")
public class MainController {

    private UserRepository userRepository;

    public MainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('USER') ")
    @PostAuthorize("returnObject.id == principal.id")
    public User userAccess(@PathVariable("id") Long id){


        return  userRepository.findUserById(id);

    }
}

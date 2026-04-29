package com.hackathon.KCThack.controller;


import com.hackathon.KCThack.config.JwtCore;
import com.hackathon.KCThack.dto.JwtResponse;
import com.hackathon.KCThack.dto.SigninRequest;
import com.hackathon.KCThack.dto.SignupRequest;
import com.hackathon.KCThack.repository.UserRepository;
import com.hackathon.KCThack.service.UserService;
import com.hackathon.KCThack.dto.ResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class SecurityController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtCore jwtCore;
    private UserService userService;


    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtCore(JwtCore jwtCore) {
        this.jwtCore = jwtCore;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest signupRequest){

        userService.userToSignUp(signupRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO("успешная регистрация"));


    }


    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signin(@RequestBody SigninRequest signinRequest) {
        String token = userService.userToSignIn(signinRequest);
        return ResponseEntity.ok(new JwtResponse(token));
    }






}

package com.hackathon.KDT_HACK.Registration;


import com.hackathon.KDT_HACK.JWT.JwtCore;
import com.hackathon.KDT_HACK.JWT.JwtResponse;
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
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest){
        if (userRepository.existsUserByUserName(signupRequest.getUserName()  )){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choose different name");
        }
        if(userRepository.existsUserByEmail(signupRequest.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choose different email");
        }

//        String hashed = passwordEncoder.encode(signupRequest.getPassword());
//        User user = new User();
//        user.setUserName(signupRequest.getUserName());
//        user.setEmail(signupRequest.getEmail());
//        user.setPassword(hashed);
//        user.setRole(UserRole.USER);
//        userRepository.save(user);
        return ResponseEntity.ok().body(userService.userToSignUp(signupRequest));


    }
    @PostMapping("/verify-email/{email}")
    public ResponseEntity<?> getVerifycationCode(@PathVariable String email)
    {
        return userService.resendVerifycationCode(email);

    }


    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody VerificationRequest request) {
        boolean verified = userService.verifyEmail(request.getEmail(), request.getCode());

        if (verified) {
            return ResponseEntity.ok("Email verified successfully");


        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signin(@RequestBody SigninRequest signinRequest) {
        String token = userService.userToSignIn(signinRequest);
        return ResponseEntity.ok(new JwtResponse(token));
    }


}

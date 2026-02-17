package com.hackathon.KDT_HACK.Registration;

import com.hackathon.KDT_HACK.JWT.JwtCore;
import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;
    private final EmailVerificationService emailVerificationService;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Lazy AuthenticationManager authenticationManager, // ⬅️ разрываем цикл
                       JwtCore jwtCore,
                       EmailVerificationService emailVerificationService
                       ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
        this.emailVerificationService = emailVerificationService;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found ", username)
        ));
        return UserDetailsImpl.build(user);
    }

    public User userToSignUp(SignupRequest signupRequest){
        String hashed = passwordEncoder.encode(signupRequest.getPassword());
        User user = new User();
        user.setFullName(signupRequest.getFullName());
        user.setUserName(signupRequest.getUserName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(hashed);
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.CONFIRMATION);
        userRepository.save(user);
        emailVerificationService.sendVerificationCode(signupRequest.getEmail());


        return user;

    }
    public ResponseEntity<?> resendVerifycationCode(String email){
        if(userRepository.findUserByEmail(email).getStatus() == UserStatus.CONFIRMATION) {
            emailVerificationService.sendVerificationCode(email);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST);
    }

    public boolean verifyEmail(String email, String verificationCode) {
        // Проверить код
        boolean isValid = emailVerificationService.verifyCode(email, verificationCode);

        if (isValid) {
            // Обновить статус пользователя
            try {
                User user = userRepository.findUserByEmail(email);


                user.setStatus(UserStatus.ACTIVE);
                userRepository.save(user);

                return true;
            }catch (Exception e){
                new Exception("Not found");
            }
        }

        return false;
    }

    public String userToSignIn(SigninRequest signinRequest){
        User user = userRepository.findUserByUserName(signinRequest.getUserName()).orElse(null);
        if(user.getStatus() != UserStatus.ACTIVE){
            throw new EntityNotFoundException("Валли");
        }
        Authentication authentication = null;

             authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signinRequest.getUserName(),
                            signinRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        return  jwtCore.generateToken(authentication);

    }

}

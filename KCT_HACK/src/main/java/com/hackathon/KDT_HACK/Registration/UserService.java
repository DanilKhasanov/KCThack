package com.hackathon.KDT_HACK.Registration;

import com.hackathon.KDT_HACK.JWT.JwtCore;
import com.hackathon.KDT_HACK.PersonalAccount.ChangePasswordRequest;
import com.hackathon.KDT_HACK.PersonalAccount.Skills.SkillRepository;
import com.hackathon.KDT_HACK.PersonalAccount.Skills.Skills;
import com.hackathon.KDT_HACK.PersonalAccount.Skills.UserSkill;
import com.hackathon.KDT_HACK.PersonalAccount.Skills.UserSkillRepository;
import com.hackathon.KDT_HACK.PersonalAccount.UpdateUserRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;
    private final UserSkillRepository userSkillRepository;
    private final SkillRepository skillRepository;


    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Lazy AuthenticationManager authenticationManager, // ⬅️ разрываем цикл
                       JwtCore jwtCore,

                       UserSkillRepository userSkillRepository,
                       SkillRepository skillRepository

                       ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
        this.userSkillRepository = userSkillRepository;
        this.skillRepository = skillRepository;

    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found ", username)
        ));
        return UserDetailsImpl.build(user);
    }


    public UserDetails loadUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return UserDetailsImpl.build(user);
    }

    public User userToSignUp(@Valid SignupRequest signupRequest){
        String hashed = passwordEncoder.encode(signupRequest.getPassword());
        User user = new User();
        user.setName(signupRequest.getName());
        user.setLastName(signupRequest.getLastname());
        user.setFullName(signupRequest.getName()+" "+signupRequest.getLastname());
        user.setUsername(signupRequest.getUsername());
        user.setBirthday(signupRequest.getBirthday());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(hashed);
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDate.now());
        user.setPoints(0);
        userRepository.save(user);


        return user;

    }


    public String userToSignIn(SigninRequest signinRequest){
        User user = userRepository.findUserByUsername(signinRequest.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BadCredentialsException("Account is blocked or deleted");
        }
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signinRequest.getUsername(), signinRequest.getPassword()
                )
        );

            SecurityContextHolder.getContext().setAuthentication(auth);
        return  jwtCore.generateToken(auth);

    }

    public User updateUser(UpdateUserRequest updateUserRequest, String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        // Проверяем уникальность нового username (если он изменён)
        if (!user.getUsername().equals(updateUserRequest.getUsername()) &&
                userRepository.existsUserByUsername(updateUserRequest.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        user.setName(updateUserRequest.getName());
        user.setLastName(updateUserRequest.getLastName());
        user.setFullName(updateUserRequest.getName()+" "+updateUserRequest.getLastName());
        user.setUsername(updateUserRequest.getUsername());
        user.setBirthday(updateUserRequest.getBirthday());
        user.setAvatar(updateUserRequest.getAvatar());
        user.setEmail(updateUserRequest.getEmail());
        user.setPhone(updateUserRequest.getPhone());
        user.setTelegram(updateUserRequest.getTelegram());
        user.setGithub(updateUserRequest.getGithub());
        user.setJob(updateUserRequest.getJob());          // поле job (было Job)
        user.setLevel(updateUserRequest.getLevel());

        // Преобразование DTO в сущности UserSkill
        List<UserSkill> newSkills = updateUserRequest.getSkills().stream()
                .map(dto -> {
                    Skills skill = new Skills();
                    skill.setId(dto.getSkillId());       // берём skillId из DTO
                    UserSkill us = new UserSkill();
                    us.setSkill(skill);
                    us.setLevel(dto.getLevel());
                    return us;
                })
                .toList();

        updateUserSkills(user, newSkills);
        return userRepository.save(user);
    }

    private void updateUserSkills(User user, List<UserSkill> newSkills) {
        List<UserSkill> currentSkills = userSkillRepository.findByUser(user);

        // Строим map новых навыков по id skill
        Map<Long, UserSkill> newSkillsMap = new HashMap<>();
        for (UserSkill us : newSkills) {
            if (us.getSkill() != null && us.getSkill().getId() != null) {
                newSkillsMap.put(us.getSkill().getId(), us);
            }
        }

        // Удаляем отсутствующие в новом списке
        for (UserSkill current : currentSkills) {
            if (!newSkillsMap.containsKey(current.getSkill().getId())) {
                userSkillRepository.delete(current);
            }
        }

        // Обрабатываем новые/обновлённые
        for (UserSkill us : newSkills) {
            Skills skill = skillRepository.findById(us.getSkill().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + us.getSkill().getId()));

            UserSkill existing = userSkillRepository.findByUserAndSkill(user, skill).orElse(null);
            if (existing != null) {
                existing.setLevel(us.getLevel());
                userSkillRepository.save(existing);
            } else {
                UserSkill newUserSkill = new UserSkill();
                newUserSkill.setUser(user);
                newUserSkill.setSkill(skill);
                newUserSkill.setLevel(us.getLevel());
                userSkillRepository.save(newUserSkill);
            }
        }
    }
    public void changePassword(String userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        // Проверяем старый пароль
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // Хешируем новый пароль и сохраняем
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


    public User findUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }
}

package com.hackathon.KDT_HACK.AdminPanel;

import com.hackathon.KDT_HACK.PersonalAccount.Skills.SkillRepository;
import com.hackathon.KDT_HACK.PersonalAccount.Skills.Skills;
import com.hackathon.KDT_HACK.Registration.User;
import com.hackathon.KDT_HACK.Registration.UserRepository;
import com.hackathon.KDT_HACK.Registration.UserStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@PreAuthorize("hasRole('ADMIN')")
public class AdminService {
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;


    public void addSkills(List<Skills> skills) {
        skillRepository.saveAll(skills);
    }

    @Transactional
    public void deleteSkills(List<Skills> skills) {
        // 1. Извлекаем имена
        List<String> names = skills.stream()
                .map(Skills::getName)
                .collect(Collectors.toList());

        // 2. Загружаем полные сущности из БД (с ID)
        List<Skills> existingSkills = skillRepository.findAllByNameIn(names);

        // 3. Проверяем, что все имена найдены
        if (existingSkills.size() != names.size()) {
            List<String> foundNames = existingSkills.stream()
                    .map(Skills::getName)
                    .toList();
            names.removeAll(foundNames);
            throw new EntityNotFoundException("Скиллы не найдены: " + names);
        }

        // 4. Удаляем загруженные сущности (у них есть ID)
        skillRepository.deleteAll(existingSkills);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User blockUserById(String id) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

            user.setStatus(UserStatus.BLOCKED);
            user.setTokenVersion(user.getTokenVersion()+1);
            userRepository.save(user);
            return user;
    }

    public User deleteUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        user.setStatus(UserStatus.DELETED);
        user.setTokenVersion(user.getTokenVersion()+1);
        userRepository.save(user);
        return user;
    }


}

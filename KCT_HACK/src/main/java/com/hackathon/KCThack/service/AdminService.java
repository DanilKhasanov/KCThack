package com.hackathon.KCThack.service;

import com.hackathon.KCThack.dto.SkillAddDto;
import com.hackathon.KCThack.dto.SkillRemoveDto;
import com.hackathon.KCThack.repository.SkillRepository;
import com.hackathon.KCThack.entity.Skills;
import com.hackathon.KCThack.entity.User;
import com.hackathon.KCThack.repository.UserRepository;
import com.hackathon.KCThack.enums.UserStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AdminService {
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;


    public void addSkills(List<SkillAddDto> skills) {

        List<Skills> newSkills = new ArrayList<>();

        for (SkillAddDto dto : skills) {
            if (!skillRepository.existsByName(dto.getName())) {
                Skills skill = new Skills();
                skill.setName(dto.getName());
                skill.setCategory(dto.getSkillsCategory());
                newSkills.add(skill);
            }
        }

        if (!newSkills.isEmpty()) {
            skillRepository.saveAll(newSkills);
        }
    }

    @Transactional
    public void deleteSkills(List<SkillRemoveDto> skills) {
        // 1. Извлекаем имена
        List<String> names = skills.stream()
                .map(SkillRemoveDto::getName)
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

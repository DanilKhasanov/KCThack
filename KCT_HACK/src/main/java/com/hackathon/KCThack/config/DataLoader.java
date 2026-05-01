package com.hackathon.KCThack.config;

import com.hackathon.KCThack.entity.Achievements;
import com.hackathon.KCThack.repository.AchievementsRepository;
import com.hackathon.KCThack.repository.SkillRepository;
import com.hackathon.KCThack.entity.Skills;
import com.hackathon.KCThack.enums.SkillsCategory;
import com.hackathon.KCThack.entity.UserSkill;
import com.hackathon.KCThack.entity.User;
import com.hackathon.KCThack.enums.*;
import com.hackathon.KCThack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    private final SkillRepository skillRepository;
    private final AchievementsRepository achievementsRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminPass;
    private final String userPass;
    private final String judgePass;

    @Autowired
    public DataLoader(SkillRepository skillRepository,
                      AchievementsRepository achievementsRepository,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      @Value("${data.admin.pass}") String adminPass,
                      @Value("${data.user.pass}") String userPass,
                      @Value("${data.judge.pass}") String judgePass) {
        this.skillRepository = skillRepository;
        this.achievementsRepository = achievementsRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminPass = adminPass;
        this.userPass = userPass;
        this.judgePass = judgePass;
    }

    @Override
    @Transactional
    public void run(String... args) {
        loadSkills();
        loadAchievements();
        loadUsers();
    }

    private void loadSkills() {
        if (skillRepository.count() > 0) return;
        List<Skills> skills = List.of(
                createSkill("Java", SkillsCategory.HARD),
                createSkill("Spring Boot", SkillsCategory.HARD),
                createSkill("Python", SkillsCategory.HARD),
                createSkill("JavaScript", SkillsCategory.HARD),
                createSkill("React", SkillsCategory.HARD),
                createSkill("SQL", SkillsCategory.HARD),
                createSkill("Git", SkillsCategory.HARD),
                createSkill("Docker", SkillsCategory.HARD),
                createSkill("HTML/CSS", SkillsCategory.HARD)
        );
        skillRepository.saveAll(skills);
        System.out.println("Loaded " + skills.size() + " skills");
    }

    private Skills createSkill(String name, SkillsCategory category) {
        Skills skill = new Skills();
        skill.setName(name);
        skill.setCategory(category);
        return skill;
    }

    private void loadAchievements() {
        if (achievementsRepository.count() > 0) return;
        List<Achievements> achievements = List.of(
                createAchievement("Новичок", "Набрать 10 очков", "novice.png", 10),
                createAchievement("Любознательный", "Набрать 50 очков", "curious.png", 50),
                createAchievement("Мастер", "Набрать 100 очков", "master.png", 100),
                createAchievement("Эксперт", "Набрать 500 очков", "expert.png", 500),
                createAchievement("Легенда", "Набрать 1000 очков", "legend.png", 1000),
                createAchievement("Первые шаги", "Набрать 5 очков", "first_steps.png", 5),
                createAchievement("Настойчивый", "Набрать 25 очков", "persistent.png", 25),
                createAchievement("Профи", "Набрать 200 очков", "pro.png", 200),
                createAchievement("Чемпион", "Набрать 750 очков", "champion.png", 750),
                createAchievement("Бессмертный", "Набрать 2000 очков", "immortal.png", 2000)
        );
        achievementsRepository.saveAll(achievements);
        System.out.println("Loaded " + achievements.size() + " achievements");
    }

    private Achievements createAchievement(String name, String description, String icon, int pointsRequired) {
        Achievements ach = new Achievements();
        ach.setName(name);
        ach.setDescription(description);
        ach.setIcon(icon);
        ach.setPointsRequired(pointsRequired);
        return ach;
    }

    private void loadUsers() {
        if (userRepository.count() > 0) {
            System.out.println("Users already exist, skipping generation.");
            return;
        }

        List<Skills> allSkills = skillRepository.findAll();
        if (allSkills.isEmpty()) {
            System.out.println("No skills found, users will be created without skills.");
        }

        User user = createUser(
                "Иван", "Петров", "ivan_user", "ivan@example.com",
                "+79123456789", "https://t.me/ivan_user", "https://github.com/ivan_user",
                LocalDate.of(1995, 6, 15), userPass, "avatar_user.png",
                UserRole.USER, UserStatus.ACTIVE, UserJob.BACK, UserLevel.BEGINNER,
                0, LocalDate.now(), Gender.MALE, allSkills
        );

        User admin = createUser(
                "Мария", "Сидорова", "maria_admin", "maria@example.com",
                "+79223334455", "https://t.me/maria_admin", "https://github.com/maria_admin",
                LocalDate.of(1988, 3, 22), adminPass, "avatar_admin.png",
                UserRole.ADMIN, UserStatus.ACTIVE, UserJob.PROJECT, UserLevel.ADVANCED,
                0, LocalDate.now(), Gender.FEMALE, allSkills
        );

        User judge = createUser(
                "Алексей", "Козлов", "alex_judge", "alex@example.com",
                "+79334445566", "https://t.me/alex_judge", "https://github.com/alex_judge",
                LocalDate.of(1992, 11, 5), judgePass, "avatar_judge.png",
                UserRole.JUDGE, UserStatus.ACTIVE, UserJob.GAME, UserLevel.INTERMEDIATE,
                0, LocalDate.now(), Gender.MALE, allSkills
        );

        userRepository.saveAll(List.of(user, admin, judge));
        System.out.println("Loaded 3 users: USER, ADMIN, JUDGE");
    }

    private User createUser(String name, String lastName, String username, String email,
                            String phone, String telegram, String github, LocalDate birthday,
                            String rawPassword, String avatar, UserRole role, UserStatus status,
                            UserJob job, UserLevel level, int points, LocalDate createdAt,
                            Gender gender, List<Skills> allSkills) {
        User user = new User();
        user.setName(name);
        user.setLastName(lastName);
        // fullName вычисляется автоматически через @PrePersist, не устанавливаем явно
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setTelegram(telegram);
        user.setGithub(github);
        user.setBirthday(birthday);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setAvatar(avatar);
        user.setRole(role);
        user.setStatus(status);
        user.setJob(job);
        user.setLevel(level);
        user.setPoints(points);
        user.setCreatedAt(createdAt);
        user.setGender(gender);
        // tokenVersion остаётся 0 (значение по умолчанию)

        if (allSkills != null && !allSkills.isEmpty()) {
            int skillCount = Math.min(3, allSkills.size());
            for (int i = 0; i < skillCount; i++) {
                UserSkill userSkill = new UserSkill();
                userSkill.setUser(user);
                userSkill.setSkill(allSkills.get(i));
                userSkill.setLevel(5 + i);
                user.getSkills().add(userSkill);
            }
        }
        return user;
    }
}
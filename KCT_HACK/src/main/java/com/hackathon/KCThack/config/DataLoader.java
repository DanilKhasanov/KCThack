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
import com.hackathon.KCThack.entity.ScheduleEntity;
import com.hackathon.KCThack.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public DataLoader(SkillRepository skillRepository,
                      AchievementsRepository achievementsRepository,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      @Value("${data.admin.pass}") String adminPass,
                      @Value("${data.user.pass}") String userPass,
                      @Value("${data.judge.pass}") String judgePass,
                      ScheduleRepository scheduleRepository) {
        this.skillRepository = skillRepository;
        this.achievementsRepository = achievementsRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminPass = adminPass;
        this.userPass = userPass;
        this.judgePass = judgePass;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        loadSkills();
        loadAchievements();
        loadUsers();
        loadSchedules();
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
                createAchievement("Первый шаг", "Набрать 0 очков", "novice.png", 0),
                createAchievement("Постоянный гость", "Набрать 50 очков", "curious.png", 10),
                createAchievement("Ветеран", "Набрать 100 очков", "master.png", 25),
                createAchievement("Железный хакер", "Набрать 500 очков", "expert.png", 50),
                createAchievement("Хакатон марафонец", "Набрать 1000 очков", "legend.png", 100)
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

        User user = createUser(
                "Иван", "Петров", "ivan_user", "ivan@example.com",
                "+79123456789", "https://t.me/ivan_user", "https://github.com/ivan_user",
                LocalDate.of(1995, 6, 15), userPass, "avatar_user.png",
                UserRole.USER, UserStatus.ACTIVE, UserJob.BACK, UserLevel.BEGINNER,
                10, LocalDate.now(), Gender.MALE, allSkills,
                "Backend разработчик, люблю Java и Spring"
        );

        User admin = createUser(
                "Мария", "Сидорова", "maria_admin", "maria@example.com",
                "+79223334455", "https://t.me/maria_admin", "https://github.com/maria_admin",
                LocalDate.of(1988, 3, 22), adminPass, "avatar_admin.png",
                UserRole.ADMIN, UserStatus.ACTIVE, UserJob.PROJECT, UserLevel.ADVANCED,
                50, LocalDate.now(), Gender.FEMALE, allSkills,
                "Project manager с опытом управления командами"
        );

        User judge = createUser(
                "Алексей", "Козлов", "alex_judge", "alex@example.com",
                "+79334445566", "https://t.me/alex_judge", "https://github.com/alex_judge",
                LocalDate.of(1992, 11, 5), judgePass, "avatar_judge.png",
                UserRole.JUDGE, UserStatus.ACTIVE, UserJob.GAME, UserLevel.INTERMEDIATE,
                30, LocalDate.now(), Gender.MALE, allSkills,
                "Game developer и судья хакатонов"
        );

        User u1 = createUser(
                "Дмитрий", "Смирнов", "dima_dev", "dima@example.com",
                "+79001112233", "https://t.me/dima_dev", "https://github.com/dima_dev",
                LocalDate.of(1998, 1, 10), userPass, "avatar1.png",
                UserRole.USER, UserStatus.ACTIVE, UserJob.FRONT, UserLevel.BEGINNER,
                15, LocalDate.now(), Gender.MALE, allSkills,
                "Frontend разработчик, люблю React"
        );

        User u2 = createUser(
                "Анна", "Иванова", "anna_code", "anna@example.com",
                "+79002223344", "https://t.me/anna_code", "https://github.com/anna_code",
                LocalDate.of(1997, 7, 20), userPass, "avatar2.png",
                UserRole.USER, UserStatus.ACTIVE, UserJob.DESIGNER, UserLevel.INTERMEDIATE,
                25, LocalDate.now(), Gender.FEMALE, allSkills,
                "UI/UX дизайнер и верстальщик"
        );

        User u3 = createUser(
                "Сергей", "Кузнецов", "sergey_it", "sergey@example.com",
                "+79003334455", "https://t.me/sergey_it", "https://github.com/sergey_it",
                LocalDate.of(1994, 9, 5), userPass, "avatar3.png",
                UserRole.USER, UserStatus.ACTIVE, UserJob.BACK, UserLevel.ADVANCED,
                60, LocalDate.now(), Gender.MALE, allSkills,
                "Senior backend developer"
        );

        User u4 = createUser(
                "Елена", "Попова", "elena_dev", "elena@example.com",
                "+79004445566", "https://t.me/elena_dev", "https://github.com/elena_dev",
                LocalDate.of(1999, 12, 12), userPass, "avatar4.png",
                UserRole.USER, UserStatus.ACTIVE, UserJob.FRONT, UserLevel.INTERMEDIATE,
                35, LocalDate.now(), Gender.FEMALE, allSkills,
                "Fullstack разработчик"
        );

        User u5 = createUser(
                "Максим", "Волков", "max_code", "max@example.com",
                "+79005556677", "https://t.me/max_code", "https://github.com/max_code",
                LocalDate.of(1996, 4, 18), userPass, "avatar5.png",
                UserRole.USER, UserStatus.ACTIVE, UserJob.BACK, UserLevel.ADVANCED,
                70, LocalDate.now(), Gender.MALE, allSkills,
                "DevOps инженер, люблю Docker и CI/CD"
        );

        userRepository.saveAll(List.of(user, admin, judge, u1, u2, u3, u4, u5));
        System.out.println("Loaded 8 users total");
    }

    private User createUser(String name, String lastName, String username, String email,
                            String phone, String telegram, String github, LocalDate birthday,
                            String rawPassword, String avatar, UserRole role, UserStatus status,
                            UserJob job, UserLevel level, int points, LocalDate createdAt,
                            Gender gender, List<Skills> allSkills,
                            String bio) {
        User user = new User();
        user.setName(name);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setBio(bio);
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

    private void loadSchedules() {
        if (scheduleRepository.count() > 0) {
            System.out.println("Schedules already exist, skipping generation.");
            return;
        }

        List<Skills> allSkills = skillRepository.findAll();
        if (allSkills.isEmpty()) {
            System.out.println("No skills in DB, cannot create schedules.");
            return;
        }

        // Вспомогательный метод поиска навыка по имени
        Skills javaSkill = findSkillByName(allSkills, "Java");
        Skills springSkill = findSkillByName(allSkills, "Spring Boot");
        Skills reactSkill = findSkillByName(allSkills, "React");
        Skills jsSkill = findSkillByName(allSkills, "JavaScript");
        Skills pythonSkill = findSkillByName(allSkills, "Python");

        ScheduleEntity hackathon1 = new ScheduleEntity();
        hackathon1.setName("Весенний хакатон 2026");
        hackathon1.setBriefDescription("Хакатон по веб-разработке");
        hackathon1.setDescription("48-часовой хакатон для создания веб-приложений с использованием Java, Spring Boot и React.");
        hackathon1.setStartDateTime(
                Instant.parse("2026-04-10T10:00:00Z")
        );

        hackathon1.setEndDateTime(
                Instant.parse("2026-04-12T18:00:00Z")
        );
        // Инициализируем список skills, т.к. в сущности он не инициализирован
        hackathon1.setSkills(new ArrayList<>());
        if (javaSkill != null) hackathon1.getSkills().add(javaSkill);
        if (springSkill != null) hackathon1.getSkills().add(springSkill);
        if (reactSkill != null) hackathon1.getSkills().add(reactSkill);

        ScheduleEntity hackathon2 = new ScheduleEntity();
        hackathon2.setName("AI/ML Хакатон 2026");
        hackathon2.setBriefDescription("Хакатон по искусственному интеллекту");
        hackathon2.setDescription("Соревнование по созданию ML-моделей и анализу данных с использованием Python.");
        hackathon2.setStartDateTime(
                Instant.parse("2026-05-05T09:00:00Z")
        );

        hackathon2.setEndDateTime(
                Instant.parse("2026-05-07T20:00:00Z")
        );
        hackathon2.setSkills(new ArrayList<>());
        if (pythonSkill != null) hackathon2.getSkills().add(pythonSkill);
        if (jsSkill != null) hackathon2.getSkills().add(jsSkill);

        scheduleRepository.saveAll(List.of(hackathon1, hackathon2));
        System.out.println("Loaded 2 schedules.");
    }

    private Skills findSkillByName(List<Skills> skills, String name) {
        return skills.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
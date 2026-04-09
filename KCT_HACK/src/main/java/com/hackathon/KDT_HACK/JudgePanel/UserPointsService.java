package com.hackathon.KDT_HACK.JudgePanel;

import com.hackathon.KDT_HACK.PersonalAccount.Achievements.Achievements;
import com.hackathon.KDT_HACK.PersonalAccount.Achievements.AchievementsRepository;
import com.hackathon.KDT_HACK.PersonalAccount.Achievements.UserAchievements;
import com.hackathon.KDT_HACK.PersonalAccount.Achievements.UserAchievementsRepository;
import com.hackathon.KDT_HACK.Registration.User;
import com.hackathon.KDT_HACK.Registration.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserPointsService {

    private final UserRepository userRepository;
    private final AchievementsRepository achievementsRepository;
    private final UserAchievementsRepository userAchievementsRepository;

    @Transactional
    public User addPoints(String userId, int pointsToAdd) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int oldPoints = user.getPoints();
        int newPoints = oldPoints + pointsToAdd;
        user.setPoints(newPoints);

        // Находим все ачивки, порог которых был пройден (старое < порог <= новое)
        List<Achievements> newAchievements = achievementsRepository
                .findByPointsRequiredBetween(oldPoints + 1, newPoints);

        for (Achievements ach : newAchievements) {
            boolean alreadyHas = userAchievementsRepository
                    .existsByUserIdAndAchievementsId(user.getId(), ach.getId());
            if (!alreadyHas) {
                UserAchievements userAch = new UserAchievements();
                userAch.setUser(user);
                userAch.setAchievements(ach);
                userAchievementsRepository.save(userAch);
                user.getAchievements().add(userAch);
            }
        }

        return userRepository.save(user);
    }
}
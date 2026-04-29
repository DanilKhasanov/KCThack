package com.hackathon.KCThack.service;

import com.hackathon.KCThack.dto.AddPointsRequest;
import com.hackathon.KCThack.entity.Achievements;
import com.hackathon.KCThack.repository.AchievementsRepository;
import com.hackathon.KCThack.entity.UserAchievements;
import com.hackathon.KCThack.repository.UserAchievementsRepository;
import com.hackathon.KCThack.entity.User;
import com.hackathon.KCThack.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserPointsService {

    private final UserRepository userRepository;
    private final AchievementsRepository achievementsRepository;
    private final UserAchievementsRepository userAchievementsRepository;
    private static final Logger log = LoggerFactory.getLogger(UserPointsService.class);

    @Transactional
    public User addPoints(String userId, AddPointsRequest pointsToAdd) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        try {


            int oldPoints = user.getPoints();
            int newPoints = oldPoints + pointsToAdd.getPoints();
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
        }catch(DataIntegrityViolationException e){
            log.debug("Caught DataAccessException");

        }
        return userRepository.save(user);


    }
}
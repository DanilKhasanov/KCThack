package com.hackathon.KCThack.repository;

import com.hackathon.KCThack.entity.UserAchievements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAchievementsRepository extends JpaRepository<UserAchievements, Long> {

    boolean existsByUserIdAndAchievementsId(String userId, Long achievementId);
}

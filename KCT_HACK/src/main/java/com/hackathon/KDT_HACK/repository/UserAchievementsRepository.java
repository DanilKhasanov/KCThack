package com.hackathon.KDT_HACK.repository;

import com.hackathon.KDT_HACK.entity.UserAchievements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAchievementsRepository extends JpaRepository<UserAchievements, Long> {

    boolean existsByUserIdAndAchievementsId(String userId, Long achievementId);
}

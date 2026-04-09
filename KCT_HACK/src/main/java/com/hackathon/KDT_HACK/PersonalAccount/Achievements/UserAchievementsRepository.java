package com.hackathon.KDT_HACK.PersonalAccount.Achievements;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAchievementsRepository extends JpaRepository<UserAchievements, Long> {

    boolean existsByUserIdAndAchievementsId(String userId, Long achievementId);
}

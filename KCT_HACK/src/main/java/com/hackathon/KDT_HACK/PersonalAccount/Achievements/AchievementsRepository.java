package com.hackathon.KDT_HACK.PersonalAccount.Achievements;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementsRepository extends JpaRepository<Achievements, Long> {
    List<Achievements> findByPointsRequiredBetween(int min, int max);
}

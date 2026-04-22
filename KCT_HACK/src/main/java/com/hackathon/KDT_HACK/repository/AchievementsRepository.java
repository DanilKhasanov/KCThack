package com.hackathon.KDT_HACK.repository;

import com.hackathon.KDT_HACK.entity.Achievements;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementsRepository extends JpaRepository<Achievements, Long> {
    List<Achievements> findByPointsRequiredBetween(int min, int max);
}

package com.hackathon.KCThack.repository;

import com.hackathon.KCThack.entity.Achievements;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementsRepository extends JpaRepository<Achievements, Long> {
    List<Achievements> findByPointsRequiredBetween(int min, int max);
}

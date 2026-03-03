package com.hackathon.KDT_HACK.Registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skills, Long> {
    List<Skills> findByCategory(Skills category);
}

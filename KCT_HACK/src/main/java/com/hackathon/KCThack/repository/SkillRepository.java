package com.hackathon.KCThack.repository;

import com.hackathon.KCThack.enums.SkillsCategory;
import com.hackathon.KCThack.entity.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skills, Long> {
    List<Skills> findByCategory(SkillsCategory category);

    boolean existsByName(String name);

    List<Skills> findAllByNameIn(List<String> names);
}

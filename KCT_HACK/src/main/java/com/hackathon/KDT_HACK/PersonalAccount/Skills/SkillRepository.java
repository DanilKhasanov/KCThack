package com.hackathon.KDT_HACK.PersonalAccount.Skills;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skills, Long> {
    List<Skills> findByCategory(SkillsCategory category);

    boolean existsByName(String name);

    List<Skills> findAllByNameIn(List<String> names);
}

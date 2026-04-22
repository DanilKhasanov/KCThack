package com.hackathon.KDT_HACK.repository;

import com.hackathon.KDT_HACK.entity.UserSkill;
import com.hackathon.KDT_HACK.entity.Skills;
import com.hackathon.KDT_HACK.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    List<UserSkill> findByUser(User user);
    Optional<UserSkill> findByUserAndSkill(User user, Skills skill);
    void deleteByUserAndSkill(User user, Skills skill);
}

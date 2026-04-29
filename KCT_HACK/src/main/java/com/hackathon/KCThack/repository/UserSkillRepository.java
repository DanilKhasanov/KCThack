package com.hackathon.KCThack.repository;

import com.hackathon.KCThack.entity.UserSkill;
import com.hackathon.KCThack.entity.Skills;
import com.hackathon.KCThack.entity.User;
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

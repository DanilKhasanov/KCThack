package com.hackathon.KCThack.repository;

import com.hackathon.KCThack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
     Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);
    Optional<User>  findUserById(String id);

    Boolean existsUserByUsername(String username);
    Boolean existsUserByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.skills s LEFT JOIN FETCH s.skill WHERE u.id = :id")
    Optional<User> findByIdWithSkills(String id);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.achievements a LEFT JOIN FETCH a.achievements WHERE u.id = :id")
    Optional<User> findByIdWithAchievements(String id);
}

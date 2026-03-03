package com.hackathon.KDT_HACK.Registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> findUserByUsername(String username);
    User findUserByEmail(String email);
    User findUserById(Long id);

    Boolean existsUserByUsername(String username);
    Boolean existsUserByEmail(String email);
}

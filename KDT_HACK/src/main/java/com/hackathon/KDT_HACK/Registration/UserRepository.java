package com.hackathon.KDT_HACK.Registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> findUserByUserName(String userName);
    User findUserByEmail(String email);
    User findUserById(Long id);

    Boolean existsUserByUserName(String userName);
    Boolean existsUserByEmail(String email);
}

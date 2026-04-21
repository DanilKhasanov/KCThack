package com.hackathon.KDT_HACK.Registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
     Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);
    Optional<User>  findUserById(String id);

    Boolean existsUserByUsername(String username);
    Boolean existsUserByEmail(String email);

}

package com.hackathon.KCThack.TeamManagement.repository;


import com.hackathon.KCThack.TeamManagement.model.TeamHackathonRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamHackathonRegistrationRepository extends JpaRepository<TeamHackathonRegistration, Long> {
    List<TeamHackathonRegistration> findByTeamId(String teamId);
    List<TeamHackathonRegistration> findByEventId(Long eventId);
    Optional<TeamHackathonRegistration> findByTeamIdAndEventId(String teamId, Long eventId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM TeamHackathonRegistration r WHERE r.team.id = :teamId")
    void deleteAllByTeamId(@Param("teamId") String teamId);
}

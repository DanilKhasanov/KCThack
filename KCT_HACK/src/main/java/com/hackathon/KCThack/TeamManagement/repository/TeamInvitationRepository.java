package com.hackathon.KCThack.TeamManagement.repository;


import com.hackathon.KCThack.TeamManagement.model.TeamInvitation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, Long> {
    List<TeamInvitation> findByInvitedUserId(String userId);
    List<TeamInvitation> findByInvitedUserIdAndStatus(String userId, TeamInvitation.Status status);
    List<TeamInvitation> findByTeamId(String teamId);
    List<TeamInvitation> findByTeamIdAndStatus(String teamId, TeamInvitation.Status status);
    boolean existsByTeamIdAndInvitedUserIdAndStatus(String teamId, String userId, TeamInvitation.Status status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM TeamInvitation i WHERE i.id = :id")
    Optional<TeamInvitation> findByIdForUpdate(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM TeamInvitation i WHERE i.team.id = :teamId")
    void deleteAllByTeamId(@Param("teamId") String teamId);
}

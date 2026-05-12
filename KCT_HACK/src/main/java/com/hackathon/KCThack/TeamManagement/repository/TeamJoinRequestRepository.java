package com.hackathon.KCThack.TeamManagement.repository;


import com.hackathon.KCThack.TeamManagement.model.TeamJoinRequest;
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
public interface TeamJoinRequestRepository extends JpaRepository<TeamJoinRequest, Long> {
    List<TeamJoinRequest> findByUserId(String userId);
    List<TeamJoinRequest> findByTeamId(String teamId);
    List<TeamJoinRequest> findByTeamIdAndStatus(String teamId, TeamJoinRequest.Status status);
    boolean existsByTeamIdAndUserIdAndStatus(String teamId, String userId, TeamJoinRequest.Status status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT j FROM TeamJoinRequest j WHERE j.id = :id")
    Optional<TeamJoinRequest> findByIdForUpdate(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM TeamJoinRequest j WHERE j.team.id = :teamId")
    void deleteAllByTeamId(@Param("teamId") String teamId);
}

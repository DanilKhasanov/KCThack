package com.hackathon.KCThack.TeamManagement.repository;

import com.hackathon.KCThack.TeamManagement.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository
        extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findByTeam_Id(String teamId);

    List<TeamMember> findByUser_Id(String userId);

    Optional<TeamMember> findByTeam_IdAndUser_Id(
            String teamId,
            String userId
    );

    boolean existsByTeam_IdAndUser_Id(
            String teamId,
            String userId
    );

    long countByTeam_Id(String teamId);

    @Modifying(clearAutomatically = true)
    @Query("""
        DELETE FROM TeamMember tm
        WHERE tm.team.id = :teamId
    """)
    void deleteAllByTeamId(@Param("teamId") String teamId);
}

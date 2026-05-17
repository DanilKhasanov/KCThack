package com.hackathon.KCThack.TeamManagement.repository;


import com.hackathon.KCThack.TeamManagement.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, String id);

    boolean existsByCreator_IdAndIsActiveTrue(String creatorId);

    List<Team> findByCreator_Id(String creatorId);


    List<Team> findByIsActiveTrue();

    Page<Team> findByIsActiveTrue(Pageable pageable);

    List<Team> findByNameContainingIgnoreCase(String query);

    @Query(value = "SELECT t FROM Team t LEFT JOIN FETCH t.creator",
            countQuery = "SELECT COUNT(t) FROM Team t")
    Page<Team> findAllWithCreator(Pageable pageable);

    @Query("""
    SELECT t FROM Team t
    WHERE t.creator.id = :userId
       OR EXISTS (
            SELECT 1 FROM TeamMember tm
            WHERE tm.team = t
              AND tm.user.id = :userId
       )
""")
    List<Team> findTeamsByUserId(@Param("userId") String userId);

    @Query("""
    SELECT t
    FROM Team t
    LEFT JOIN FETCH t.creator
    WHERE t.id = :id
""")
    Optional<Team> findByIdWithCreator(@Param("id") String id);

    @Query("""
    SELECT DISTINCT t
    FROM Team t
    LEFT JOIN FETCH t.creator
    WHERE t.creator.id = :userId
       OR EXISTS (
            SELECT 1 FROM TeamMember tm
            WHERE tm.team = t
              AND tm.user.id = :userId
       )
""")
    List<Team> findTeamsByUserIdWithCreator(@Param("userId") String userId);
}



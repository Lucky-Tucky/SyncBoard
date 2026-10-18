package com.backend.SyncBoard.Repository;

import com.backend.SyncBoard.Model.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @Query("select p from Project p inner join p.members m where p.id = :id and m.id = :userId")
    Optional<Project> findByUserIdAndProjectId(@Param("userId") UUID userId , @Param("id") UUID id);

    @Query("select p from Project p inner join p.members m where m.id= :userId")
    List<Project> findAllProject(@Param("userId") UUID userId, Pageable pageable);
}

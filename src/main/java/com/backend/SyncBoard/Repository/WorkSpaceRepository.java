package com.backend.SyncBoard.Repository;

import com.backend.SyncBoard.Model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkSpaceRepository extends JpaRepository<Workspace, UUID> {

    @Query("select w from Workspace as w where w.user.id = :userId and w.id = :id")
    public Optional<Workspace> findByIdAndWorkSpaceId(@Param("id") UUID id , @Param("userId") UUID userId);
}

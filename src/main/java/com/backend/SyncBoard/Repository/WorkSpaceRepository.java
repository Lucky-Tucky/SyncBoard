package com.backend.SyncBoard.Repository;

import com.backend.SyncBoard.Model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;
import java.util.*;

@Repository
public interface WorkSpaceRepository extends JpaRepository<Workspace, UUID> {

    @Query("select w from Workspace as w where w.user.id = :userId and w.id = :id")
    public Optional<Workspace> findByIdAndWorkSpaceId(@Param("id") UUID id , @Param("userId") UUID userId);

    @Query("select w from Workspace as w where w.user.id=:userId")
    public List<Workspace> findAllWorkSpace(@Param("userId") UUID userId, Pageable pageable);
}

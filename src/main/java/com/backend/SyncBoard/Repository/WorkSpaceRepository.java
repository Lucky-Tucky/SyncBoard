package com.backend.SyncBoard.Repository;

import com.backend.SyncBoard.Model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkSpaceRepository extends JpaRepository<Workspace, UUID> {
}

package com.backend.SyncBoard.Repository;

import com.backend.SyncBoard.Model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
}

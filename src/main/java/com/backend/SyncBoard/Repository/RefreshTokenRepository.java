package com.backend.SyncBoard.Repository;

import com.backend.SyncBoard.Model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
//    RefreshToken findByUser_Id(int userId);
}

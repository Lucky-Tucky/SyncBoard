package com.backend.SyncBoard.Repository;

import com.backend.SyncBoard.Model.RefreshToken;
import com.backend.SyncBoard.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}


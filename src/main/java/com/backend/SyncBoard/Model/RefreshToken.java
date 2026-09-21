package com.backend.SyncBoard.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class RefreshToken {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @OneToOne(mappedBy = "refreshToken", fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String token;

    @LastModifiedDate
    private  LocalDateTime modifiedAt;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isRevoked = false;

    @PrePersist
    protected void createdAt(){
        this.createdAt = LocalDateTime.now();
    }
}

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id",nullable = false,referencedColumnName = "userTokenRelation")
    private User user;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String token;
//
//    @Column(nullable = false)
//    private LocalDateTime expiryAt;

    @LastModifiedDate
    private  LocalDateTime modifiedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isRevoked = false;
}

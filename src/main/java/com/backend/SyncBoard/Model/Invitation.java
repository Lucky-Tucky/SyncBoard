package com.backend.SyncBoard.Model;

import com.backend.SyncBoard.Enum.InvitationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String email;

    @JsonIgnore
    @ManyToOne
    private Project project;

    @JsonIgnore
    @ManyToOne
    private User invitedBy;

    @JsonIgnore
    @Column(nullable = false, unique = true)
    private String token;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private InvitationStatus status = InvitationStatus.PENDING;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @Column(updatable = false,nullable = false)
    private LocalDateTime expiresAt;

    @PrePersist
    private void updateTime(){
        if(this.createdAt==null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}


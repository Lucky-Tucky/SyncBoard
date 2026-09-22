package com.backend.SyncBoard.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(
        uniqueConstraints = {
            @UniqueConstraint(name = "unique_key_user",columnNames = {"user_workspace_key","slug"})
        }
)
public class Workspace {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Column(nullable = false,length = 255)
    private String name;

    private int issued_value;

    @Column(nullable = false,length = 64,unique = true)
    private String slug;

    @Column(nullable = true)
    private String tags;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "user_workspace_key",nullable = false,referencedColumnName = "id")
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void  onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}

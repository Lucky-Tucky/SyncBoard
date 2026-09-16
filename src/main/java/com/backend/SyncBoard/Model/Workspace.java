package com.backend.SyncBoard.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Workspace {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Column(nullable = false,length = 255)
    private String name;

    @Column(nullable = false,length = 64,unique = true)
    private String slug;

    @ManyToOne()
    @JoinColumn(name = "user_workspace_key",nullable = false,referencedColumnName = "id")
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;
}

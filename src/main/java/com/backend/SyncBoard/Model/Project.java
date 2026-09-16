package com.backend.SyncBoard.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        uniqueConstraints = {
            @UniqueConstraint(name="unq_key_workspaceid",columnNames = {"key","project_workspace_key"})
        }
)
public class Project {


    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Column(nullable = false,length = 255)
    private String name;

    @Column(nullable = false, length = 16)
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_workspace_key",nullable = false,referencedColumnName = "id")
    private Workspace workspace;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;
}

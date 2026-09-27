package com.backend.SyncBoard.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
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

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminUser" , nullable = false, updatable = false, referencedColumnName = "id")
    private User adminUser;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private java.util.Set<User> members = new java.util.HashSet<>();

    @PrePersist
    private void setDateTime(){
        if(this.createdAt==null){
            this.createdAt = LocalDateTime.now();
        }else{
            this.modifiedAt = LocalDateTime.now();
        }
    }

}

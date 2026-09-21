package com.backend.SyncBoard.Model;

import com.backend.SyncBoard.Enum.Roles;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="UserInfo")
public class User {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;

    @OneToOne(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JoinColumn(name = "userTokenRelation",nullable = true,referencedColumnName = "id")
    private RefreshToken refreshToken;
}

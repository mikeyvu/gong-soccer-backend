package com.mikey.gongsoccer.entity.user;

import com.mikey.gongsoccer.entity.team.TeamEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="users")
@DynamicUpdate
@DynamicInsert
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String passwordHash;

    @Column(precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false)
    private boolean isVerified = false;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity teamEntity;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();
}

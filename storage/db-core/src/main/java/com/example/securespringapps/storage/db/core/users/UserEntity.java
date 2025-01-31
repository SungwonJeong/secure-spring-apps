package com.example.securespringapps.storage.db.core.users;

import com.example.securespringapps.core.enums.UserRole;
import com.example.securespringapps.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder
    private UserEntity(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static UserEntity generate(String username, String password, UserRole role) {
        return UserEntity.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
    }
}

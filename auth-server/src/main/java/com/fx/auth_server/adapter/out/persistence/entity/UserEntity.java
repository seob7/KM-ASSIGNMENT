package com.fx.auth_server.adapter.out.persistence.entity;

import com.fx.auth_server.domain.User;
import com.fx.auth_server.domain.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // OAuth 인 경우 null

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public static UserEntity from(User user) {
        return UserEntity.builder()
            .id(user.id())
            .email(user.email())
            .password(user.password())
            .nickname(user.nickname())
            .role(user.role())
            .build();
    }

    public User toDomain() {
        return new User(
            this.getId(),
            this.email,
            this.password,
            this.nickname,
            this.role
        );
    }

}

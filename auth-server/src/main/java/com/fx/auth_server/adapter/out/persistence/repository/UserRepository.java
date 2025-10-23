package com.fx.auth_server.adapter.out.persistence.repository;

import com.fx.auth_server.adapter.out.persistence.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);

    Optional<UserEntity> findByEmail(String email);

}

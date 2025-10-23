package com.fx.auth_server.adapter.out.persistence;

import com.fx.auth_server.adapter.out.persistence.entity.UserEntity;
import com.fx.auth_server.adapter.out.persistence.repository.UserRepository;
import com.fx.auth_server.application.port.out.UserPersistencePort;
import com.fx.auth_server.domain.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//@PersistenceAdapter
@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {

    private final UserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(UserEntity.from(user));
    }

    @Override
    public Boolean existsEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId).map(UserEntity::toDomain);
    }

}

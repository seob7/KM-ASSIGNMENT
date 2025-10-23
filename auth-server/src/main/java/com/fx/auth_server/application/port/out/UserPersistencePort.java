package com.fx.auth_server.application.port.out;

import com.fx.auth_server.domain.User;
import java.util.Optional;

public interface UserPersistencePort {

    void save(User user);

    Boolean existsEmail(String email);

    Boolean existsNickname(String nickname);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long userId);
}

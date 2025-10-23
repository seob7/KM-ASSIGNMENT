package com.fx.auth_server.application.service;

import com.fx.auth_server.application.port.in.UserQueryUseCase;
import com.fx.auth_server.application.port.out.UserPersistencePort;
import com.fx.auth_server.common.exception.UserException;
import com.fx.auth_server.common.exception.errorcode.UserErrorCode;
import com.fx.auth_server.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService implements UserQueryUseCase {

    private final UserPersistencePort userPersistencePort;

    @Override
    public User getUser(Long userId) {
        return userPersistencePort.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

}

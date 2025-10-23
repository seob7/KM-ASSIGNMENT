package com.fx.auth_server.application.service;

import com.fx.auth_server.application.port.in.UserCommandUseCase;
import com.fx.auth_server.application.port.in.dto.UserLoginCommand;
import com.fx.auth_server.application.port.in.dto.UserRegisterCommand;
import com.fx.auth_server.application.port.out.JwtProviderPort;
import com.fx.auth_server.application.port.out.PasswordEncoderPort;
import com.fx.auth_server.application.port.out.TokenStoragePort;
import com.fx.auth_server.application.port.out.UserPersistencePort;
import com.fx.auth_server.domain.TokenInfo;
import com.fx.auth_server.domain.User;
import com.fx.auth_server.common.exception.UserException;
import com.fx.auth_server.common.exception.errorcode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService implements UserCommandUseCase {

    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final JwtProviderPort jwtProviderPort;
    private final TokenStoragePort tokenStoragePort;

    /**
     * 1. email 존재 유무 확인 2. 저장
     *
     * @param userRegisterCommand
     * @return true | false
     */
    @Override
    @Transactional
    public Boolean register(UserRegisterCommand userRegisterCommand) {
        if (userPersistencePort.existsEmail(userRegisterCommand.email())) {
            throw new UserException(UserErrorCode.EMAIL_EXISTS);
        }
        if (userPersistencePort.existsNickname(userRegisterCommand.nickname())) {
            throw new UserException(UserErrorCode.NICKNAME_EXISTS);
        }

        String encodedPassword = passwordEncoderPort.encode(userRegisterCommand.password());
        userPersistencePort.save(User.registerUser(userRegisterCommand, encodedPassword));
        return true;
    }

    @Override
    public TokenInfo login(UserLoginCommand userLoginCommand) {
        User user = userPersistencePort.findByEmail(userLoginCommand.email())
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if(!passwordEncoderPort.matches(userLoginCommand.password(), user.password())) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }
        TokenInfo tokenInfo = jwtProviderPort.generateTokens(user.id(), user.role());

        tokenStoragePort.save(tokenInfo.refreshToken());
        return tokenInfo;
    }

    @Override
    public Boolean logout() {
        tokenStoragePort.remove();
        return true;
    }

    @Override
    public TokenInfo reIssueToken(String bearerRefreshToken) {
        return jwtProviderPort.reIssueToken(bearerRefreshToken);
    }
}

package com.fx.auth_server.application.port.in;

import com.fx.auth_server.application.port.in.dto.UserLoginCommand;
import com.fx.auth_server.application.port.in.dto.UserRegisterCommand;
import com.fx.auth_server.domain.TokenInfo;

/**
 * User Input port
 *
 * @author SEOB
 * @since 2025-10-22
 */
public interface UserCommandUseCase {

    Boolean register(UserRegisterCommand userRegisterCommand);

    TokenInfo login(UserLoginCommand userLoginCommand);

    Boolean logout();

    TokenInfo reIssueToken(String bearerRefreshToken);
}

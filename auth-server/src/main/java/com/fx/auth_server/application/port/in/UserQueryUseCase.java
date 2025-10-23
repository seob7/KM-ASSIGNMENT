package com.fx.auth_server.application.port.in;

import com.fx.auth_server.domain.User;

/**
 * User Input port
 *
 * @author SEOB
 * @since 2025-10-22
 */
public interface UserQueryUseCase {

    User getUser(Long userId);

}

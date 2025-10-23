package com.fx.auth_server.adapter.out.security;

import com.fx.auth_server.application.port.out.PasswordEncoderPort;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderAdapter implements PasswordEncoderPort {

    private static final int LOG_ROUNDS = 12;

    @Override
    public String encode(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(LOG_ROUNDS));
    }

    @Override
    public Boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}

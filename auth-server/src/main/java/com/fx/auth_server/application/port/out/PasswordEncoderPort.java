package com.fx.auth_server.application.port.out;

public interface PasswordEncoderPort {

    String encode(String rawPassword);

    Boolean matches(String rawPassword, String encodedPassword);

}

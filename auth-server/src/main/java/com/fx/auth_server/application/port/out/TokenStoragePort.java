package com.fx.auth_server.application.port.out;

public interface TokenStoragePort {

    void save(String refreshToken);

    String getToken();

    void remove();

}

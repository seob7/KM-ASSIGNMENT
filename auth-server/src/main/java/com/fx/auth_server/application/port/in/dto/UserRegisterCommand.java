package com.fx.auth_server.application.port.in.dto;

public record UserRegisterCommand(
    String email,
    String password,
    String nickname
) {

}

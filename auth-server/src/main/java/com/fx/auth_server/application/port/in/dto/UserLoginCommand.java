package com.fx.auth_server.application.port.in.dto;

public record UserLoginCommand(
    String email,
    String password
) {

}

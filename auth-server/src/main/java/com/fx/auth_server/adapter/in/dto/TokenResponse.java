package com.fx.auth_server.adapter.in.dto;

public record TokenResponse(
    String accessToken,
    String refreshToken
) {

}

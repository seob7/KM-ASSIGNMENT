package com.fx.auth_server.adapter.in;

import com.fx.auth_server.adapter.in.dto.TokenResponse;
import com.fx.auth_server.adapter.in.dto.UserLoginRequest;
import com.fx.auth_server.adapter.in.dto.UserRegisterRequest;
import com.fx.auth_server.application.port.in.UserCommandUseCase;
import com.fx.auth_server.domain.TokenInfo;
import io.github.seob7.Api;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/open-api/users")
public class UserOpenApiAdapter {

    private final UserCommandUseCase userCommandUseCase;

    @PostMapping
    public ResponseEntity<Api<Boolean>> register(
        @RequestBody @Valid UserRegisterRequest userRegisterRequest
    ) {
        return Api.OK(userCommandUseCase.register(userRegisterRequest.toCommand()),
            "회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<Api<TokenResponse>> login(
        @RequestBody @Valid UserLoginRequest userLoginRequest
    ) {
        TokenInfo tokenInfo = userCommandUseCase.login(userLoginRequest.toCommand());
        return Api.OK(new TokenResponse(tokenInfo.accessToken(), tokenInfo.refreshToken()), "로그인 되었습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<Api<Boolean>> logout() {
        return Api.OK(userCommandUseCase.logout(), "로그아웃되었습니다.");
    }

    @PostMapping("/reissue")
    public ResponseEntity<Api<TokenInfo>> reissueToken(
        @RequestHeader("Authorization") String bearerRefreshToken
    ) {
        return Api.OK(userCommandUseCase.reIssueToken(bearerRefreshToken));
    }

    /**
     * - 만료된 경우 아래와 같은 응답
     * {
     *     "metaData": {
     *         "success": false,
     *         "code": 401,
     *         "message": "만료된 토큰입니다."
     *     },
     *     "data": null
     * }
     */

}

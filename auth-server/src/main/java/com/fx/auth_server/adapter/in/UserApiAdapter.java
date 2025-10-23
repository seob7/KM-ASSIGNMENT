package com.fx.auth_server.adapter.in;

import com.fx.auth_server.application.port.in.UserQueryUseCase;
import com.fx.auth_server.common.annotation.AuthenticatedUser;
import com.fx.auth_server.common.resolver.AuthUser;
import io.github.seob7.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/users")
public class UserApiAdapter {

    private final UserQueryUseCase userQueryUseCase;

    @GetMapping()
    public ResponseEntity<Api<String>> getNickname(@AuthenticatedUser AuthUser authUser) {
        return Api.OK(userQueryUseCase.getUser(authUser.userId()).nickname());
    }

}

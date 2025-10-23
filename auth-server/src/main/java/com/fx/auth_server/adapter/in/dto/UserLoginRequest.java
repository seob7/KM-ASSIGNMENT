package com.fx.auth_server.adapter.in.dto;

import com.fx.auth_server.application.port.in.dto.UserLoginCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserLoginRequest(

    @NotBlank(message = "필수 입력 사항입니다.")
    @Email(message ="이메일 형식이 올바르지 않습니다.")
    @Size(max = 200, message = "이메일은 200자 이내여야 합니다.")
    String email,

    @NotBlank(message = "필수 입력 사항입니다.")
    @Size(min = 5, max = 20, message = "비밀번호는 5자 이상 20자 이하여야 합니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]*$",
        message = "비밀번호는 영문과 숫자를 포함해야 합니다."
    )
    String password
) {

    public UserLoginCommand toCommand() {
        return new UserLoginCommand(email, password);
    }

}
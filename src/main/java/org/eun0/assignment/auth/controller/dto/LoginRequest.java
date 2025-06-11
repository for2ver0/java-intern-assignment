package org.eun0.assignment.auth.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotNull(message = "유저네임 입력은 필수입니다.")
    private String username;

    @NotNull(message = "비밀번호 입력은 필수입니다.")
    private String password;

    private LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static LoginRequest of(String username, String password) {
        return new LoginRequest(username, password);
    }

}

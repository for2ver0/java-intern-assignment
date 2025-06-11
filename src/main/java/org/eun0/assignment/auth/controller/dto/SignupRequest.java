package org.eun0.assignment.auth.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequest {

    @NotNull(message = "유저네임 입력은 필수입니다.")
    @Size(min = 3, message = "유저네임은 3자 이상으로 설정해야 합니다.")
    @Size(max = 100, message = "유저네임은 100자 이하로 설정해야 합니다.")
    private String username;

    @NotNull(message = "비밀번호 입력은 필수입니다.")
    @Size(min = 8, max = 30, message = "비밀번호는 8자 이상, 30자 이하로 설정해야 합니다.")
    private String password;

    @NotNull(message = "닉네임 입력은 필수입니다.")
    @Size(min = 3, message = "닉네임은 3자 이상으로 설정해야 합니다.")
    @Size(max = 100, message = "닉네임은 100자 이하로 설정해야 합니다.")
    private String nickname;

    private SignupRequest(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public static SignupRequest of(String username, String password, String nickname) {
        return new SignupRequest(username, password, nickname);
    }
}

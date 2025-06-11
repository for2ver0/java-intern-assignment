package org.eun0.assignment.auth.controller.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private String token;

    private LoginResponse(String token) {
        this.token = token;
    }

    public static LoginResponse of(String token) {
        return new LoginResponse(token);
    }

}

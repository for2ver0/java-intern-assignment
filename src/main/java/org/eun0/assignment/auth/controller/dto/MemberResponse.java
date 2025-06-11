package org.eun0.assignment.auth.controller.dto;

import lombok.Getter;
import org.eun0.assignment.auth.enums.Role;

import java.util.List;

@Getter
public class MemberResponse {

    private String username;
    private String nickname;
    private List<Role> roles;

    private MemberResponse(String username, String nickname, List<Role> roles) {
        this.username = username;
        this.nickname = nickname;
        this.roles = roles;
    }

    public static MemberResponse of(String username, String nickname, List<Role> roles) {
        return new MemberResponse(username, nickname, roles);
    }

}

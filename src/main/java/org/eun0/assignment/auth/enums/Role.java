package org.eun0.assignment.auth.enums;

import lombok.Getter;

@Getter
public enum Role {

    USER("USER"),
    ADMIN("ADMIN");

    private final String authorityName;

    Role(String authorityName) {
        this.authorityName = authorityName;
    }

}

package org.eun0.assignment.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eun0.assignment.auth.enums.Role;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "role")
    private List<Role> roles = new ArrayList<>();

    private Member(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.roles.add(Role.USER);
    }

    private Member(String username, String password, String nickname, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.roles = roles;
    }

    public static Member of(String username, String password, String nickname) {
        return new Member(username, password, nickname);
    }

    public static Member of(String username, String password, String nickname, List<Role> roles) {
        return new Member(username, password, nickname, roles);
    }

    public void promoteToAdmin() {
        this.getRoles().clear();
        this.getRoles().add(Role.ADMIN);
    }

}

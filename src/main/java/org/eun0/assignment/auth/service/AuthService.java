package org.eun0.assignment.auth.service;

import lombok.RequiredArgsConstructor;
import org.eun0.assignment.auth.controller.dto.LoginRequest;
import org.eun0.assignment.auth.controller.dto.LoginResponse;
import org.eun0.assignment.auth.controller.dto.MemberResponse;
import org.eun0.assignment.auth.controller.dto.SignupRequest;
import org.eun0.assignment.auth.entity.Member;
import org.eun0.assignment.auth.respository.MemberRepository;
import org.eun0.assignment.common.exception.ResponseException;
import org.eun0.assignment.common.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.eun0.assignment.common.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public MemberResponse register(SignupRequest signupRequest) {
        if (memberRepository.existsByUsername(signupRequest.getUsername())) {
            throw ResponseException.of(USER_ALREADY_EXISTS);
        }

        String encodePassword = passwordEncoder.encode(signupRequest.getPassword());

        Member member = Member.of(signupRequest.getUsername(), encodePassword, signupRequest.getNickname());
        memberRepository.save(member);

        return MemberResponse.of(member.getUsername(), member.getNickname(), member.getRoles());
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> ResponseException.of(USER_NOT_FOUND));

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw ResponseException.of(INVALID_CREDENTIALS);
        }

        String token = jwtUtil.createToken(member.getId(), member.getUsername(), member.getNickname(), member.getRoles());

        return LoginResponse.of(token);
    }

    public MemberResponse promoteToAdmin(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> ResponseException.of(USER_NOT_FOUND));

        member.promoteToAdmin();

        return MemberResponse.of(member.getUsername(), member.getNickname(), member.getRoles());
    }
}

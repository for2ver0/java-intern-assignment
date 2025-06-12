package org.eun0.assignment.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eun0.assignment.auth.controller.dto.LoginRequest;
import org.eun0.assignment.auth.controller.dto.LoginResponse;
import org.eun0.assignment.auth.controller.dto.MemberResponse;
import org.eun0.assignment.auth.controller.dto.SignupRequest;
import org.eun0.assignment.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 API", description = "회원가입 및 로그인 API")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "회원가입",
            description = "새로운 사용자 정보를 등록합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "409", description = "이미 가입된 사용자"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력값")
    })
    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        MemberResponse signupResponse = authService.register(signupRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(signupResponse);
    }

    @Operation(
            summary = "로그인",
            description = "사용자 인증 과정을 통해 토큰을 발급합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "올바르지 않은 아이디 또는 비밀번호"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse memberResponse = authService.login(loginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberResponse);
    }

}

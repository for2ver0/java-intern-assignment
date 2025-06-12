package org.eun0.assignment.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.eun0.assignment.auth.controller.dto.MemberResponse;
import org.eun0.assignment.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관리자 API", description = "관리자 권한이 필요한 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AuthService authService;

    @Operation(
            summary = "사용자 관리자 권한 부여",
            description = "관리자가 특정 사용자에게 관리자 권한을 부여합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "권한 부여 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없는 일반 사용자"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    })
    @PatchMapping("/users/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponse> grantAdminRole(@PathVariable Long userId) {
        MemberResponse memberResponse = authService.promoteToAdmin(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberResponse);
    }

}

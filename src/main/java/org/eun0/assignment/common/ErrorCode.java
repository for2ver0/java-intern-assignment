package org.eun0.assignment.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    UNKNOWN_ERROR("C000", "알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT("C001", "잘못된 입력값입니다.", HttpStatus.BAD_REQUEST),

    // Token
    TOKEN_EMPTY("T000", "토큰이 비어있습니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_FOUND("T001", "존재하지 않는 토큰입니다.", HttpStatus.NOT_FOUND),
    INVALID_TOKEN_TYPE("T002", "유효하지 않은 형식의 토큰입니다.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("T002", "유효하지 않은 인증 토큰입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("T004", "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_PARSING_ERROR("T005", "토큰 파싱 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // Auth
    ACCESS_DENIED("A001", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_CREDENTIALS("A002", "아이디 또는 비밀번호가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    // User (회원 가입하지 않은 사용자)
    USER_NOT_FOUND("U000", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("U001", "이미 가입된 사용자입니다.", HttpStatus.CONFLICT);

    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}

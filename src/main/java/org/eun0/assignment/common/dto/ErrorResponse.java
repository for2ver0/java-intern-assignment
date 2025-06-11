package org.eun0.assignment.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.eun0.assignment.common.ErrorCode;
import org.eun0.assignment.common.exception.ResponseException;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private String errorCode;
    private String message;
    private String url;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private ErrorResponse(ErrorCode errorCode, String url) {
        this.errorCode = errorCode.getErrorCode();
        this.message = errorCode.getMessage();
        this.url = url;
        this.timestamp = LocalDateTime.now();
    }

    private ErrorResponse(ErrorCode errorCode, String message, String url) {
        this.errorCode = errorCode.getErrorCode();
        this.message = errorCode.getMessage() + ": " + message;
        this.url = url;
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(ResponseException e, String url) {
        return new ErrorResponse(e.getErrorCode(), url);
    }

    public static ErrorResponse of(ErrorCode errorCode, String url) {
        return new ErrorResponse(errorCode, url);
    }

    public static ErrorResponse of(ErrorCode errorCode, String message, String url) {
        return new ErrorResponse(errorCode, message, url);
    }

}
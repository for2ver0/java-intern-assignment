package org.eun0.assignment.common.exception;

import lombok.Getter;
import org.eun0.assignment.common.ErrorCode;

@Getter
public class ResponseException extends RuntimeException {

    private final ErrorCode errorCode;

    private ResponseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static ResponseException of(ErrorCode errorCode) {
        return new ResponseException(errorCode);
    }

}

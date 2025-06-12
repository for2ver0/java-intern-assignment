package org.eun0.assignment.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.eun0.assignment.common.ErrorCode;
import org.eun0.assignment.common.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return baseException(request, errorMessage);
    }

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<ErrorResponse> handleResponseException(HttpServletRequest request, ResponseException e) {
        return baseException(request, e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception e) {
        return baseException(request, ErrorCode.UNKNOWN_ERROR);
    }

    private ResponseEntity<ErrorResponse> baseException(HttpServletRequest request, String errorMessage) {
        String url = request.getRequestURI();

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT, errorMessage, url));
    }

    private ResponseEntity<ErrorResponse> baseException(HttpServletRequest request, ErrorCode errorCode) {
        String url = request.getRequestURI();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, url));
    }

    private ResponseEntity<ErrorResponse> baseException(HttpServletRequest request, ResponseException e) {
        String url = request.getRequestURI();

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e, url));
    }

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorResponse> BaseException(IllegalArgumentException e, HttpServletRequest request) {
//        ErrorResponse error = ErrorResponse.of(
//                I,
//                e.getMessage(),
//                request.getRequestURI()
//        );
//
//        return ResponseEntity.badRequest().body(error);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> BaseException(HttpServletRequest request, Exception e) {
//
//        return BaseException(ErrorCode.INTERNAL_SERVER_ERROR, e, request);
//    }

}
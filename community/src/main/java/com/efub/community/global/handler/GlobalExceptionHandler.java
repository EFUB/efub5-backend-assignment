package com.efub.community.global.handler;

import com.efub.community.member.dto.ErrorResponseDto;
import com.efub.community.global.exception.BlogException;
import com.efub.community.global.exception.ExceptionCode;
import com.efub.community.global.exception.dto.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BlogException.class)
    public ResponseEntity<ExceptionResponse> handleBlogException(BlogException exception,
                                                                 HttpServletRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                request.getMethod(),
                request.getRequestURI(),
                exception.getClientExceptionCodeName(),
                exception.getMessage()
        );

        return ResponseEntity.status(exception.getHttpStatusCode())
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException runtimeException,
                                                                    HttpServletRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                request.getMethod(),
                request.getRequestURI(),
                ExceptionCode.INTERNAL_SERVER_ERROR.getClientExceptionCode().name(),
                ExceptionCode.INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
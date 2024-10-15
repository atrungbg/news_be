package com.example.NewBackEnd.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        System.out.println("Handling BaseException with message: " + ex.getErrorMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode(),
                ex.getErrorMessage(),
                ex.getErrorStatus()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        System.out.println("Handling Exception with message: " + ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                500,
                ex.getMessage(),
                "Internal Server Error"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = (fieldError != null) ? fieldError.getDefaultMessage() : "Validation error";

        ErrorResponse errorResponse = new ErrorResponse(
                400,
                errorMessage,
                "Bad Request"
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        System.out.println("Handling AccessDeniedException with message: " + ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                403,
                ex.getMessage(),
                "Forbidden"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        System.out.println("Handling AuthenticationException with message: " + ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                401,
                ex.getMessage(),
                "Unauthorized"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    public void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
        System.out.println("Handling AccessDeniedException with message: " + ex.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        ErrorResponse errorResponse = new ErrorResponse(403, ex.getMessage(), "Forbidden");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    public void handleAuthenticationException(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        System.out.println("Handling AuthenticationException with message: " + ex.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        ErrorResponse errorResponse = new ErrorResponse(401, ex.getMessage(), "Unauthorized");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
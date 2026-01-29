package com.minisupportdesk.error;

import com.minisupportdesk.auth.DTO.SignUpResponseDTO;
import io.jsonwebtoken.JwtException;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST);
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
        ApiError apiError = ApiError.builder()
                .timeStamp(LocalDateTime.now())
                .statusCode(HttpStatus.UNAUTHORIZED)
                .error(ex.getMessage())
                .message("Invalid username or password")
                .build();
        return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException ex) {
        ApiError apiError = ApiError.builder()
                .error(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .statusCode(HttpStatus.UNAUTHORIZED)
                .message("Invalid JWT token")
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError>handleIllegalArgumentException(EmailAlreadyExistsException ex){
        ApiError apiError = ApiError.builder()
                .timeStamp(LocalDateTime.now())
                .statusCode(HttpStatus.CONFLICT)
                .message(ex.getMessage())
                .error(ex.getMessage())
                .build();
        return new ResponseEntity<>(apiError,HttpStatus.CONFLICT);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        ApiError apiError = ApiError.builder()
                .timeStamp(LocalDateTime.now())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .error(ex.getMessage())
                .message("An unexpected error occurred: ")
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(OptimisticEntityLockException.class)
    public ResponseEntity<ApiError> handleOptimisticEntityLockException(Exception ex){
         ApiError apiError = ApiError.builder()
                .timeStamp(LocalDateTime.now())
                .statusCode(HttpStatus.CONFLICT)
                .error(ex.getMessage())
                .message("Data was updated by another user. Please refresh.")
                .build();

         return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

}

package io.github.go.gerenciamento_adocoes.common.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest req) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), req.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> databaseError(DatabaseException e, HttpServletRequest req) {
        String error = "Database error";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), req.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationError> validationError(ValidationException e, HttpServletRequest req) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidationError error = new ValidationError(
                Instant.now(),
                status.value(),
                "Validation error",
                e.getMessage(),
                req.getRequestURI(),
                e.getCamposInvalidos()
        );
        return ResponseEntity.status(status).body(error);
    }
}

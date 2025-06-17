package dev.wortel.meshok.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private ErrorType errorType;
    private String message;
    private Instant timestamp;

    ErrorResponse(ErrorType errorType, String message) {
        this.errorType = errorType;
        this.message = message;
        timestamp = Instant.now();
    }
}
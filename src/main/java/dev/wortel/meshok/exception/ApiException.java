package dev.wortel.meshok.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ApiException extends ResponseStatusException {
    public ApiException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason); // 400 Bad Request по умолчанию
    }
}

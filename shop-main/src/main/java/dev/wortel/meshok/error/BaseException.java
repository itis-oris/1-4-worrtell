package dev.wortel.meshok.error;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final ErrorType errorType;

    public BaseException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }
}
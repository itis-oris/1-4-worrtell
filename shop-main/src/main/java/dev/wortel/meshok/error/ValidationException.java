package dev.wortel.meshok.error;

public class ValidationException extends BaseException {
    public ValidationException(String message) { super(ErrorType.VALIDATION, message); }
}
package dev.wortel.meshok.error;

public class SecurityException extends BaseException {
    public SecurityException(String message) { super(ErrorType.SECURITY, message); }
}
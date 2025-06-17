package dev.wortel.meshok.error;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String message) { super(ErrorType.NOT_FOUND, message); }
}

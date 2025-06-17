package dev.wortel.meshok.error;

import lombok.Getter;

@Getter
public class BusinessException extends BaseException {
    public BusinessException(String message) { super(ErrorType.BUSINESS, message); }
}
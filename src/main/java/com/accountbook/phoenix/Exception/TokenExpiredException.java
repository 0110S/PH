package com.accountbook.phoenix.Exception;

public class TokenExpiredException extends Throwable {
    public TokenExpiredException(String message) {
        super(message);
    }
}

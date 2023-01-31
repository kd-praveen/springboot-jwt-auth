package com.security.jwt.exceptions;

public class JwtTokenExpiredException extends RuntimeException{
    private static final long serialVersionUID = 1;

    public JwtTokenExpiredException(String message) {
        super(message);
    }
}

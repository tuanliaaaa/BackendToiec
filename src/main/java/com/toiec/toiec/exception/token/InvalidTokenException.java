package com.toiec.toiec.exception.token;


import com.toiec.toiec.exception.base.UnauthorizedException;

public class InvalidTokenException extends UnauthorizedException {
    public InvalidTokenException(String message) {
        setMessage(message);
    }
}
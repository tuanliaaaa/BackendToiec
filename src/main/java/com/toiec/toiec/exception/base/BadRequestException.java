package com.toiec.toiec.exception.base;

import java.util.Map;

public class BadRequestException extends BaseException{
    public BadRequestException(String message) {
        this.setMessage(message);
    }

}

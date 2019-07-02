package com.example.wzt.share.controller.Response;

public class NotFoundException extends GlobalException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, int code) {
        super(message, code);
    }
}

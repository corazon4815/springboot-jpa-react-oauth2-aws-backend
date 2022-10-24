package com.web.springboot.common.exception;

public class CustomException extends RuntimeException {

    private static final long serialVersionUID=1L;

    public CustomException(String message) {
        super(message);
    }

    public CustomException() {

    }
}

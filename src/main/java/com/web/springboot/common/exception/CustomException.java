package com.web.springboot.common.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomException extends RuntimeException {

    private static final long serialVersionUID=1L;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(Exception e) {
        super(e.getMessage());
    }
}

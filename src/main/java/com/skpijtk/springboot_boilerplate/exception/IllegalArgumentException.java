package com.skpijtk.springboot_boilerplate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalArgumentException extends RuntimeException {
    
    private final String messageId;

    public IllegalArgumentException(String message, String messageId) {
        super(message);
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }
}

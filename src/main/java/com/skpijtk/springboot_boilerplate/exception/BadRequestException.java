package com.skpijtk.springboot_boilerplate.exception;

public class BadRequestException extends RuntimeException {

    private final String messageId;

    public BadRequestException(String message, String messageId) {
        super(message);
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }
}

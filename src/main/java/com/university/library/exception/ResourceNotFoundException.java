package com.university.library.exception;

public class ResourceNotFoundException extends RuntimeException{
    public String getBodyMessage() {
        return bodyMessage;
    }

    private final String bodyMessage;

    public ResourceNotFoundException(String bodyMessage) {
        this.bodyMessage = bodyMessage;
    }


}

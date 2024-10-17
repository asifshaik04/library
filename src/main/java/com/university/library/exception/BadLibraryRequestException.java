package com.university.library.exception;

public class BadLibraryRequestException extends RuntimeException{
    public String getBodyMessage() {
        return bodyMessage;
    }

    private final String bodyMessage;

    public BadLibraryRequestException(String bodyMessage) {
        this.bodyMessage = bodyMessage;
    }


}

package com.university.library.exception;

public class InvalidAuthorNameException  extends BadLibraryRequestException{
    public InvalidAuthorNameException(String authorName) {
        super(String.format("The Author name %s is not valid", authorName));
    }
}

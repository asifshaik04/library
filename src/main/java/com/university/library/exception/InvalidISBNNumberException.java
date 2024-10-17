package com.university.library.exception;

public class InvalidISBNNumberException extends BadLibraryRequestException{
    public InvalidISBNNumberException(String isbn) {
        super(String.format("The ISBN %s is not valid", isbn));
    }
}

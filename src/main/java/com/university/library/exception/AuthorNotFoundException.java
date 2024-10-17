package com.university.library.exception;

public class AuthorNotFoundException extends ResourceNotFoundException {
    public AuthorNotFoundException(String authorName) {
        super(String.format("The Author with name %s is not found", authorName));
    }
}

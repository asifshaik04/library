package com.university.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BookNotFoundException extends ResourceNotFoundException {
    public BookNotFoundException(String isbn) {
        super(String.format("The Book with ISBN %s is not found", isbn));
    }
}

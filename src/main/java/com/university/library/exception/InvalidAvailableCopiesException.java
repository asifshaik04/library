package com.university.library.exception;

public class InvalidAvailableCopiesException extends  Exception{
    public InvalidAvailableCopiesException(String isbn) {
        super(String.format("Cannot further decrement available copies of book with ISBN %s", isbn));
    }
}

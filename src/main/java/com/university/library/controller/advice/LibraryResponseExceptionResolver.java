package com.university.library.controller.advice;

import com.university.library.exception.BadLibraryRequestException;
import com.university.library.exception.InvalidAvailableCopiesException;
import com.university.library.exception.ResourceNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class LibraryResponseExceptionResolver {


    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<HttpStatus> handleResourceNotFoundExceptions(ResourceNotFoundException ex) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BadLibraryRequestException.class})
    public ResponseEntity<HttpStatus> handleBadRequestExceptions(BadLibraryRequestException ex) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidAvailableCopiesException.class})
    public ResponseEntity<HttpStatus> handleAvailableCopiesBadRequestExceptions(InvalidAvailableCopiesException ex) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
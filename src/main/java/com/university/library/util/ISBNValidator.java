package com.university.library.util;

import com.university.library.exception.InvalidISBNNumberException;

import java.util.Objects;
import java.util.regex.Pattern;

public class ISBNValidator {

    /*
    TODO To make this as an annotation i.e @ISBNValidator instead of utility class
     */
    private ISBNValidator(){
    }

    private static final String ISBN_REGEX = "^978-\\d-\\d{2,5}-\\d{1,7}-\\d$";

    public static Boolean isValidISBN(String isbn){
        return Pattern.compile(ISBN_REGEX).matcher(isbn).matches();
    }

    public static Boolean isNotValidISBN(String isbn){
        return !isValidISBN(isbn);
    }

    public static void validateISBN(String isbn) {
        if (Objects.isNull(isbn) || isNotValidISBN(isbn)){
            throw new InvalidISBNNumberException(isbn);
        }
    }
}

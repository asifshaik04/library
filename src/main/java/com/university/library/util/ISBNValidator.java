package com.university.library.util;

import java.util.regex.Pattern;

public class ISBNValidator {

    private static final String ISBN_REGEX = "^978-\\d-\\d{2,5}-\\d{1,7}-\\d$";

    public static Boolean isValidISBN(String isbn){
        return Pattern.compile(ISBN_REGEX).matcher(isbn).matches();
    }

    public static Boolean isNotValidISBN(String isbn){
        return !isValidISBN(isbn);
    }
}

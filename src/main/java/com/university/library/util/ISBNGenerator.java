package com.university.library.util;

import java.util.Random;

public class ISBNGenerator {
    //TO-DO
    // Can make a proper robust isbn generator also write parametarised unit tests
    public static String generateISBN() {
        return String.join("-", "978", "5", "158499",String.valueOf(new Random().nextInt(10)));
    }
}

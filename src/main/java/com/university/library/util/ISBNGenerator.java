package com.university.library.util;

import java.util.Random;
import java.util.UUID;


public class ISBNGenerator {

    private ISBNGenerator(){

    }
    //TO-DO
    // Can make a proper robust isbn generator also write parametarised unit tests
    public static String generateISBN() {
        Random random = new Random();
        return String.join("-", "978", "0", String.valueOf(random.nextInt(1000)),"21357",String.valueOf(random.nextInt(10)));
    }
}

package com.university.library;

import com.university.library.util.ISBNGenerator;
import com.university.library.util.ISBNValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ISBNValidatorTest {



    @ParameterizedTest
    @MethodSource("validISBNs")
    void testIsValidISBN(String isbn){
        assertThat(ISBNValidator.isValidISBN(isbn), is(equalTo(true)));
        IntStream.range(0, 10).forEach(value -> ISBNGenerator.generateISBN());
    }

    @Test
    void testDynamicIsValidISBN(){
        IntStream.range(0, 10).forEach(value -> assertThat(ISBNValidator.isValidISBN(ISBNGenerator.generateISBN()), is(equalTo(true))));
    }

    private static Stream<String> validISBNs(){

        return Stream.of(ISBNGenerator.generateISBN(), "978-0-553-21311-7", "978-3-16-148410-0", "978-0-7432-7356-5", "978-0-452-28423-4","978-1-5011-9181-4","978-0-141-19054-3",
                "978-0-307-27778-3","978-0-06-112008-4","978-0-525-65829-9","978-0-553-21311-7", "978-1-4391-6734-7");
    }

    @ParameterizedTest
    @ValueSource(strings = { "978-5-158499-4"})
    void testIsInValidISBN(String isbn){
        assertThat(ISBNValidator.isNotValidISBN(isbn), is(equalTo(true)));
    }
}

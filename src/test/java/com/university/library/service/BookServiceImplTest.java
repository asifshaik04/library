package com.university.library.service;

import com.university.library.dto.BookDTO;
import com.university.library.dto.BookResource;
import com.university.library.exception.BookNotFoundException;
import com.university.library.exception.InvalidAuthorNameException;
import com.university.library.exception.InvalidAvailableCopiesException;
import com.university.library.exception.InvalidISBNNumberException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookServiceImplTest {

    private final BookService bookService = new BookServiceImpl();

    private final String GATSBY_ISBN = "978-3-16-148410-0";
    private final String INVALID_ISBN = "978-3-16-148410";

    @Test
    void getBookByISBNSuccess(){
        BookDTO bookDTO = bookService.getBookByISBN(GATSBY_ISBN);
        assertThat(bookDTO, is(notNullValue()));
        assertThat(bookDTO.isbn(), is(equalTo(GATSBY_ISBN)));
        assertThat(bookDTO.title(), is(equalTo("The Great Gatsby")));
        assertThat(bookDTO.author(), is(equalTo("F. Scott Fitzgerald")));
        assertThat(bookDTO.publicationYear(), is(equalTo(1925)));
        assertThat(bookDTO.availableCopies(), is(equalTo(12)));
    }

    @Test
    void getBookByISBNThrowsInvalidISBNNumberException(){
        InvalidISBNNumberException exception = assertThrows(InvalidISBNNumberException.class, () -> bookService.getBookByISBN(null));

        assertThat(exception, is(notNullValue()));
        assertThat(exception.getBodyMessage(), is(equalTo("The ISBN null is not valid")));
    }

    @Test
    void getBookByISBNThrowsBookNotFoundException(){
        String isbn = "978-3-16-148410-9";
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> bookService.getBookByISBN(isbn));

        assertThat(exception, is(notNullValue()));
        assertThat(exception.getBodyMessage(), is(equalTo(String.format("The Book with ISBN %s is not found", isbn))));
    }

    @Test
    void getBooksByAuthorSuccess(){
        String authorName = "F. Scott Fitzgerald";
        List<BookDTO> books = bookService.getBooksByAuthor(authorName);
        assertThat(books, is(notNullValue()));
        assertThat(books.size(), is(equalTo(1)));
    }

    @Test
    void getBooksByAuthorSuccessEmptyList(){
        String authorName = "Random Name";
        List<BookDTO> books = bookService.getBooksByAuthor(authorName);
        assertThat(books, is(notNullValue()));
        assertThat(books.size(), is(equalTo(0)));
    }

    @Test
    void getBooksByAuthorThrowsInvalidAuthorNameException(){
        InvalidAuthorNameException exception = assertThrows(InvalidAuthorNameException.class, () -> bookService.getBooksByAuthor(null));

        assertThat(exception, is(notNullValue()));
        assertThat(exception.getBodyMessage(), is(equalTo("The Author name null is not valid")));
    }

    @Test
    void removeBookByISBNSuccess(){
        BookDTO bookDTO = bookService.getBookByISBN(GATSBY_ISBN);
        assertThat(bookDTO, is(notNullValue()));
        bookService.removeBookByISBN(GATSBY_ISBN);
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> bookService.getBookByISBN(GATSBY_ISBN));
        assertThat(exception, is(notNullValue()));
        assertThat(exception.getBodyMessage(), is(equalTo(String.format("The Book with ISBN %s is not found", GATSBY_ISBN))));
    }

    @Test
    void removeBookWithNullISBNFailure(){
        InvalidISBNNumberException exception = assertThrows(InvalidISBNNumberException.class, () ->  bookService.removeBookByISBN(null));
        assertThat(exception, is(notNullValue()));
        assertThat(exception.getBodyMessage(), is(equalTo(String.format("The ISBN %s is not valid", null))));
    }

    @Test
    void removeBookWithInvalidISBNFailure(){
        InvalidISBNNumberException exception = assertThrows(InvalidISBNNumberException.class, () ->  bookService.removeBookByISBN(INVALID_ISBN));
        assertThat(exception, is(notNullValue()));
        assertThat(exception.getBodyMessage(), is(equalTo(String.format("The ISBN %s is not valid", INVALID_ISBN))));
    }

    @Test
    void createBookSuccess(){
        BookDTO bookDTO = bookService.createBook(new BookResource("Harry Potter", "J K Rowling",2001,100));
        assertThat(bookDTO, is(notNullValue()));
        assertThat(bookDTO.isbn(), is(notNullValue()));
//        assertThat(ISBNValidator.isValidISBN(bookDTO.isbn()), is(equalTo(true)));
        assertThat(bookDTO.title(), is(equalTo("Harry Potter")));
        assertThat(bookDTO.author(), is(equalTo("J K Rowling")));
        assertThat(bookDTO.publicationYear(), is(equalTo(2001)));
        assertThat(bookDTO.availableCopies(), is(equalTo(100)));

    }

    @Test
    void incrementAvailableCopiesSuccess(){
        BookDTO bookDTO = bookService.getBookByISBN(GATSBY_ISBN);
        assertThat(bookDTO, is(notNullValue()));
        assertThat(bookDTO.isbn(), is(equalTo(GATSBY_ISBN)));
        int availableCopiesBefore = bookDTO.availableCopies();

        bookService.incrementAvailableCopies(GATSBY_ISBN);

        bookDTO = bookService.getBookByISBN(GATSBY_ISBN);
        assertThat(bookDTO, is(notNullValue()));
        assertThat(bookDTO.isbn(), is(equalTo(GATSBY_ISBN)));
        int availableCopiesAfter = bookDTO.availableCopies();
        assertThat(availableCopiesAfter, is(equalTo(availableCopiesBefore+1)));


    }

    @Test
    void decrementAvailableCopiesSuccess() throws InvalidAvailableCopiesException {
        BookDTO bookDTO = bookService.getBookByISBN(GATSBY_ISBN);
        assertThat(bookDTO, is(notNullValue()));
        assertThat(bookDTO.isbn(), is(equalTo(GATSBY_ISBN)));
        int availableCopiesBefore = bookDTO.availableCopies();

        bookService.decrementAvailableCopies(GATSBY_ISBN);

        bookDTO = bookService.getBookByISBN(GATSBY_ISBN);
        assertThat(bookDTO, is(notNullValue()));
        assertThat(bookDTO.isbn(), is(equalTo(GATSBY_ISBN)));
        int availableCopiesAfter = bookDTO.availableCopies();
        assertThat(availableCopiesAfter, is(equalTo(availableCopiesBefore-1)));
    }

    @Test
    void decrementAvailableCopiesFailure(){
        //
        String isbnWithZeroCopies = "978-1-5011-9181-4";
        InvalidAvailableCopiesException exception = assertThrows(InvalidAvailableCopiesException.class, () ->  bookService.decrementAvailableCopies(isbnWithZeroCopies));
        assertThat(exception, is(notNullValue()));
        assertThat(exception.getMessage(), is(equalTo(String.format("Cannot further decrement available copies of book with ISBN %s", isbnWithZeroCopies))));
    }

}

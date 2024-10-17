package com.university.library.service;

import com.university.library.domain.Book;
import com.university.library.dto.BookDTO;
import com.university.library.dto.BookResource;
import com.university.library.exception.BookNotFoundException;
import com.university.library.exception.InvalidAuthorNameException;
import com.university.library.exception.InvalidAvailableCopiesException;
import com.university.library.exception.InvalidISBNNumberException;
import com.university.library.util.ISBNGenerator;
import com.university.library.util.ISBNValidator;
import org.springframework.stereotype.Service;
//import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService{

    private final ArrayList<Book> allBooks = new ArrayList<>(List.of(
            new Book("978-3-16-148410-0",	"The Great Gatsby", "F. Scott Fitzgerald",1925,12),
                new Book("978-0-7432-7356-5",	"To Kill a Mockingbird", "Harper Lee",1960,5),
                new Book("978-0-452-28423-4",	"1984", "George Orwell",1949,7),
                new Book("978-1-5011-9181-4",	"Where the Crawdads Sing", "Delia Owens",2018,0),
                new Book("978-0-141-19054-3",	"Pride and Prejudice", "Jane Austen",1813,10),
                new Book("978-0-307-27778-3",	"The Road", "Cormac McCarthy",2006,4),
                new Book("978-0-06-112008-4",	"Brave New World", "Aldous Huxley",1932,6),
                new Book("978-0-525-65829-9",	"The Silent Patient", "Alex Michaelides",2019,9),
                new Book("978-0-553-21311-7",	"A Game of Thrones", "George R.R. Martin",1996,3),
                new Book("978-1-4391-6734-7",	"The Hunger Games", "Suzanne Collins",2008,15)
                ));
    @Override
//    @Transactional
    public BookDTO getBookByISBN(String isbn) {
        validateISBN(isbn);
        Book bookDomain = findBook(isbn);
        return new BookDTO( bookDomain.getIsbn(), bookDomain.getTitle(), bookDomain.getAuthor(),
                        bookDomain.getPublicationYear(), bookDomain.getAvailableCopies());
    }

    private Book findBook(String isbn) {
        Optional<Book> bookDomainOptional =  allBooks.stream()
                .filter(book -> book.getIsbn().equals(isbn)).findAny();

        if (bookDomainOptional.isEmpty()){
             throw new BookNotFoundException(isbn);
        }
        return bookDomainOptional.get();
    }

    private static void validateISBN(String isbn) {
        if (Objects.isNull(isbn) || ISBNValidator.isNotValidISBN(isbn)){
            throw new InvalidISBNNumberException(isbn);
        }
    }

    @Override
//    @Transactional
    public List<BookDTO> getBooksByAuthor(String authorName) {
        if (Objects.isNull(authorName)){
            throw new InvalidAuthorNameException(null);
        }
        return allBooks.stream()
                .filter(book -> book.getAuthor().equals(authorName))
                .map(bookDomain -> new BookDTO( bookDomain.getIsbn(), bookDomain.getTitle(), bookDomain.getAuthor(), bookDomain.getPublicationYear(), bookDomain.getAvailableCopies()))
                .collect(Collectors.toList());
    }

    @Override
//    @Transactional
    public void removeBookByISBN(String isbn) {
        validateISBN(isbn);
        allBooks.remove(findBook(isbn));

    }

//    @Transactional
    @Override
    public BookDTO createBook(BookResource bookResource) {
        Book book = new Book(ISBNGenerator.generateISBN(), bookResource.title(), bookResource.author(), bookResource.publicationYear(), bookResource.availableCopies());
        allBooks.add(book);
        return new BookDTO(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getAvailableCopies());
    }

    @Override
//    @Transactional
    public BookDTO incrementAvailableCopies(String isbn) {
        validateISBN(isbn);
        Book book = findBook(isbn);
        book.incrementAvailableCopies();
        return new BookDTO(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getAvailableCopies());
    }

    @Override
//    @Transactional
    public BookDTO decrementAvailableCopies(String isbn) throws InvalidAvailableCopiesException {
        validateISBN(isbn);
        Book book = findBook(isbn);
        book.decrementAvailableCopies();
        return new BookDTO(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getAvailableCopies());
    }
}

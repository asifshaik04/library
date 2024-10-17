package com.university.library.service;

import com.university.library.dto.BookDTO;
import com.university.library.dto.BookResource;
import com.university.library.exception.InvalidAvailableCopiesException;

import java.util.List;

public interface BookService {

    public BookDTO getBookByISBN(String isbn);

    List<BookDTO> getBooksByAuthor(String authorName);

    void removeBookByISBN(String isbn);

    BookDTO createBook(BookResource bookResource);

    BookDTO incrementAvailableCopies(String isbn);

    BookDTO decrementAvailableCopies(String isbn) throws InvalidAvailableCopiesException;
}

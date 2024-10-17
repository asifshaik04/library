package com.university.library.domain;


import com.university.library.exception.InvalidAuthorNameException;
import com.university.library.exception.InvalidAvailableCopiesException;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private Integer publicationYear;
    private Integer availableCopies;

    public Book(String isbn, String title, String author, Integer publicationYear, Integer availableCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.availableCopies = availableCopies;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void incrementAvailableCopies() {
        ++availableCopies;
    }

    public void decrementAvailableCopies() throws InvalidAvailableCopiesException {
        if (availableCopies == 0){
           throw new InvalidAvailableCopiesException(isbn);
        }
        --availableCopies;
    }
}

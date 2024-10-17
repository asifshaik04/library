package com.university.library.controller;

import com.university.library.dto.BookDTO;
import com.university.library.dto.BookResource;
import com.university.library.exception.InvalidAuthorNameException;
import com.university.library.exception.InvalidAvailableCopiesException;
import com.university.library.exception.InvalidISBNNumberException;
import com.university.library.service.BookService;
import com.university.library.util.ISBNValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookAPIController {

    private final BookService bookService;
    public BookAPIController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Retrieves a book's details using its ISBN number.
     *
     * @param isbnNumber The ISBN number of the book.
     * @return ResponseEntity containing the BookDTO object with the book details.
     * @throws InvalidISBNNumberException if the ISBN number is not valid.
     *
     * Example usage: GET /books/978-3-16-148410-0
     */
    @GetMapping("/books/{isbn}")
    public ResponseEntity<BookDTO> findBookByISBN(@NonNull @PathVariable("isbn")  String isbnNumber){
        if(ISBNValidator.isNotValidISBN(isbnNumber)){
            throw new InvalidISBNNumberException(isbnNumber);
        }

        return ResponseEntity.ok(bookService.getBookByISBN(isbnNumber));
    }

    /**
     * Retrieves a list of books written by a specific author.
     *
     * @param authorName The name of the author to search books for.
     * @return ResponseEntity containing a list of BookDTO objects written by the author.
     * @throws InvalidAuthorNameException if the author name is empty.
     *
     * Example usage: GET /books?authorName=J%20K%20Rowling
     */
    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> findBookByAuthorName(@NonNull @RequestParam(value = "authorName")  String authorName){
        if(authorName.isEmpty()){
            throw new InvalidAuthorNameException(authorName);
        }

        return ResponseEntity.ok(bookService.getBooksByAuthor(authorName));
    }

    /**
     * Deletes a book from the library's collection by its ISBN number.
     *
     * @param isbn The ISBN number of the book to be deleted.
     * @throws InvalidISBNNumberException if the ISBN number is not valid.
     *
     * Example usage: DELETE /books/978-3-16-148410-0
     */
    @DeleteMapping("/books/{isbn}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeBook(@NonNull @PathVariable(value = "isbn")  String isbn){
        if(ISBNValidator.isNotValidISBN(isbn)){
            throw new InvalidISBNNumberException(isbn);
        }
        bookService.removeBookByISBN(isbn);
    }

    /**
     * Adds a new book to the library collection.
     *
     * @param bookResource The BookResource object containing details of the new book.
     * @return ResponseEntity containing the created BookDTO object.
     * <p>
     * Example usage: POST /books
     * Request body:
     * {
     *   "isbn": "978-3-16-148410-0",
     *   "title": "Harry Potter",
     *   "author": "J K Rowling",
     *   "publicationYear": 2001,
     *   "availableCopies": 100
     * }
     */
    @PostMapping("/books")
    public ResponseEntity<BookDTO> createBookResource(@RequestBody BookResource bookResource){

        return ResponseEntity.ok(bookService.createBook(bookResource));
    }

    /**
     * Increments the available copies of a specific book by 1.
     *
     * @param isbn The ISBN number of the book to increment available copies.
     * @return ResponseEntity containing the updated BookDTO object.
     *
     * Example usage: PATCH /books/978-3-16-148410-0/available-copies/increment
     */
    @PatchMapping("/books/{isbn}/available-copies/increment")
    public ResponseEntity<BookDTO> incrementAvailableCopiesOfBook(@NonNull @PathVariable(value = "isbn") String isbn){
            return ResponseEntity.ok(bookService.incrementAvailableCopies(isbn));
    }

    /**
     * Decrements the available copies of a specific book by 1.
     *
     * @param isbn The ISBN number of the book to decrement available copies.
     * @return ResponseEntity containing the updated BookDTO object.
     * @throws InvalidAvailableCopiesException if decrementing results in negative available copies.
     *
     * Example usage: PATCH /books/978-3-16-148410-0/available-copies/decrement
     */
    @PatchMapping("/books/{isbn}/available-copies/decrement")
    public ResponseEntity<BookDTO> decrementAvailableCopiesOfBook(@NonNull @PathVariable(value = "isbn") String isbn) throws InvalidAvailableCopiesException {
        return ResponseEntity.ok(bookService.decrementAvailableCopies(isbn));
    }
}

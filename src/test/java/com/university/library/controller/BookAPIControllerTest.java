package com.university.library.controller;

import com.university.library.controller.advice.LibraryResponseExceptionResolver;
import com.university.library.dto.BookDTO;
import com.university.library.dto.BookResource;
import com.university.library.exception.AuthorNotFoundException;
import com.university.library.exception.BookNotFoundException;
import com.university.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
//import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class BookAPIControllerTest {

    public static final String INVALID_ISBN = "978-3-16-148410";
    private MockMvc bookMockMvc;

    @InjectMocks
    private BookAPIController bookAPIController;

    @Mock
    private BookService mockBookService;

    private final String GATSBY_ISBN = "978-3-16-148410-0";
    private final String GATSBY_AUTHOR = "F. Scott Fitzgerald";

    @BeforeEach
    void  setUp() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
        bookMockMvc = MockMvcBuilders.standaloneSetup(bookAPIController)
                .setControllerAdvice(LibraryResponseExceptionResolver.class)
                .build();
    }

    @Test
    void shouldGetSingleBookForGivenISBNSuccess() throws Exception {
        BookDTO bookDTO = new BookDTO("978-3-16-148410-0", "The Great Gatsby", "F. Scott Fitzgerald", 1925, 12);
        Mockito.when(mockBookService.getBookByISBN(GATSBY_ISBN))
                .thenReturn(bookDTO);

        MockHttpServletResponse response = bookMockMvc.perform(MockMvcRequestBuilders
            .get(String.format("/api/books/%s", GATSBY_ISBN))
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

      assertThat(response.getStatus(), is(equalTo(HttpStatus.OK.value())));
      assertThat(response.getContentAsString(), is(equalTo("{\"isbn\":\""+bookDTO.isbn()+"\",\"title\":\""+bookDTO.title()+"\",\"author\":\"F. Scott Fitzgerald\",\"publicationYear\":1925,\"availableCopies\":12}")));
    }

    @Test
    void shouldThrowBookNotFoundException404Failure() throws Exception {
        Mockito.when(mockBookService.getBookByISBN(GATSBY_ISBN))
                .thenThrow(new BookNotFoundException(GATSBY_ISBN));

        MockHttpServletResponse response = bookMockMvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/books/%s", GATSBY_ISBN))
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertThat(response.getStatus(), is(equalTo(HttpStatus.NOT_FOUND.value())));
    }

    @Test
    void shouldThrowBadISBNRequestException400Failure() throws Exception {
        MockHttpServletResponse response = bookMockMvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/books/%s", INVALID_ISBN))
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertThat(response.getStatus(), is(equalTo(HttpStatus.BAD_REQUEST.value())));
    }



    @Test
    void shouldGetBooksForGivenAuthorNameSuccess() throws Exception {
        List<BookDTO> books = List.of(
                new BookDTO("978-3-16-148410-0",	"The Great Gatsby", "F. Scott Fitzgerald",1925,12),
                new BookDTO("978-0-7432-7356-5",	"To Kill a Mockingbird", "F. Scott Fitzgerald",1960,5)
        );

        Mockito.when(mockBookService.getBooksByAuthor(GATSBY_AUTHOR))
                .thenReturn(books);

        MockHttpServletResponse response = bookMockMvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/books?authorName=%s", GATSBY_AUTHOR))
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertThat(response.getStatus(), is(equalTo(HttpStatus.OK.value())));
        assertThat(response.getContentAsString(), is(equalTo("[" +
                "{\"isbn\":\""+books.getFirst().isbn()+"\",\"title\":\""+books.getFirst().title()+"\",\"author\":\""+books.getFirst().author()+"\",\"publicationYear\":"+books.getFirst().publicationYear()+",\"availableCopies\":"+books.getFirst().availableCopies()+"}," +
                "{\"isbn\":\""+books.getLast().isbn()+"\",\"title\":\""+books.getLast().title()+"\",\"author\":\""+books.getLast().author()+"\",\"publicationYear\":"+books.getLast().publicationYear()+",\"availableCopies\":"+books.getLast().availableCopies()+"}" +
                "]")));
    }

    @Test
    void shouldGetEmptyBooksForGivenAuthorNameSuccess() throws Exception {
        List<BookDTO> books = List.of();

        Mockito.when(mockBookService.getBooksByAuthor(GATSBY_AUTHOR))
                .thenReturn(books);

        MockHttpServletResponse response = bookMockMvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/books?authorName=%s", GATSBY_AUTHOR))
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertThat(response.getStatus(), is(equalTo(HttpStatus.OK.value())));
        assertThat(response.getContentAsString(), is(equalTo("[]")));

    }

    @Test
    void shouldThrowAuthorNotFoundException404Failure() throws Exception {

        Mockito.when(mockBookService.getBooksByAuthor(GATSBY_AUTHOR))
                .thenThrow(new AuthorNotFoundException(GATSBY_AUTHOR));

        MockHttpServletResponse response = bookMockMvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/books?authorName=%s", GATSBY_AUTHOR))
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();


        assertThat(response.getStatus(), is(equalTo(HttpStatus.NOT_FOUND.value())));
    }

    @Test
    void shouldThrowAuthorNameRequestException400Failure() throws Exception {

        MockHttpServletResponse response = bookMockMvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/books?authorName=%s", ""))
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();


        assertThat(response.getStatus(), is(equalTo(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    void shouldRemoveBooksForGivenISBNSuccess() throws Exception {

        MockHttpServletResponse response = bookMockMvc.perform(MockMvcRequestBuilders
                .delete(String.format("/api/books/%s", GATSBY_ISBN))
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertThat(response.getStatus(), is(equalTo(HttpStatus.NO_CONTENT.value())));

    }

    @Test
    void shouldThrowBadISBNRequestForRemoveBookException400Failure() throws Exception {

        MockHttpServletResponse response = bookMockMvc.perform(MockMvcRequestBuilders
                .delete(String.format("/api/books/%s", INVALID_ISBN))
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertThat(response.getStatus(), is(equalTo(HttpStatus.BAD_REQUEST.value())));

    }

    @Test
    void shouldCreateBookSuccess() throws Exception {

        Mockito.when(mockBookService.createBook(any(BookResource.class)))
                .thenReturn(new BookDTO("978-3-16-148410-9",	"Harry Potter", "J K Rowling",2001,100));

        MockHttpServletResponse response = bookMockMvc.perform(MockMvcRequestBuilders
                .post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Harry Potter\",\"author\": \"J K Rowling\",\"publicationYear\":2001,\"availableCopies\":100}")
                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();

        assertThat(response.getContentAsString(), is(equalTo("{\"isbn\":\"978-3-16-148410-9\",\"title\":\"Harry Potter\",\"author\":\"J K Rowling\",\"publicationYear\":2001,\"availableCopies\":100}")));
    }

    /*
    @Test
    void shouldReturnBadRequestForCreateBookFailure() throws Exception {

        MockHttpServletResponse response = bookMockMvc.perform(MockMvcRequestBuilders
                .post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Harry Potter\",\"author\": \"J K Rowling\",\"publicationYear\":2001,\"availableCopies\":0}")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn().getResponse();
    }*/

    @Test
    void incrementAvailableCopiesOfBookSuccess() throws Exception {
        BookDTO bookDTO = new BookDTO("978-3-16-148410-0",	"The Great Gatsby", "F. Scott Fitzgerald",1925,13);
        Mockito.when(mockBookService.incrementAvailableCopies(GATSBY_ISBN))
                .thenReturn(bookDTO);

        MockHttpServletResponse response = bookMockMvc.perform(MockMvcRequestBuilders
                .patch(String.format("/api/books/%s/available-copies/increment", GATSBY_ISBN))
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertThat(response.getStatus(), is(equalTo(HttpStatus.OK.value())));
        assertThat(response.getContentAsString(), is(equalTo("{\"isbn\":\""+bookDTO.isbn()+"\",\"title\":\""+bookDTO.title()+"\",\"author\":\""+bookDTO.author()+"\",\"publicationYear\":"+bookDTO.publicationYear()+",\"availableCopies\":"+bookDTO.availableCopies()+"}")));
    }

    @Test
    void decrementAvailableCopiesOfBookSuccess() throws Exception {
        BookDTO bookDTO = new BookDTO("978-3-16-148410-0",	"The Great Gatsby", "F. Scott Fitzgerald",1925,13);
        Mockito.when(mockBookService.decrementAvailableCopies(GATSBY_ISBN))
                .thenReturn(bookDTO);

        MockHttpServletResponse response = bookMockMvc.perform(MockMvcRequestBuilders
                .patch(String.format("/api/books/%s/available-copies/decrement", GATSBY_ISBN))
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertThat(response.getStatus(), is(equalTo(HttpStatus.OK.value())));
        assertThat(response.getContentAsString(), is(equalTo("{\"isbn\":\""+bookDTO.isbn()+"\",\"title\":\""+bookDTO.title()+"\",\"author\":\""+bookDTO.author()+"\",\"publicationYear\":"+bookDTO.publicationYear()+",\"availableCopies\":"+bookDTO.availableCopies()+"}")));
    }


}

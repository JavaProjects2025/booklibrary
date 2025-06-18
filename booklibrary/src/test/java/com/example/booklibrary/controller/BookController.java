package com.example.booklibrary.controller;

import com.example.booklibrary.model.Book;
import com.example.booklibrary.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Book testBook;
    private final String BOOK_ID = "1234567890";

    @BeforeEach
    void setUp() {
        // Register JavaTimeModule for LocalDate serialization
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Set up MockMvc with standalone setup and custom ObjectMapper
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        testBook = new Book();
        testBook.setId(BOOK_ID);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setIsbn("123-4567890123");
        testBook.setPublishedDate(LocalDate.now());
    }

    @Test
    void createBook_ShouldReturnCreatedStatusAndBook() throws Exception {
        when(bookService.saveBook(any(Book.class))).thenReturn(testBook);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(BOOK_ID))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"));

        verify(bookService, times(1)).saveBook(any(Book.class));
    }

    @Test
    void getBookById_ShouldReturnBook() throws Exception {
        when(bookService.getBookById(BOOK_ID)).thenReturn(testBook);

        mockMvc.perform(get("/books/{id}", BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOOK_ID))
                .andExpect(jsonPath("$.title").value("Test Book"));

        verify(bookService, times(1)).getBookById(BOOK_ID);
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook() throws Exception {
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setIsbn("987-6543210987");
        updatedBook.setPublishedDate(LocalDate.now());

        when(bookService.updateBook(eq(BOOK_ID), any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/books/{id}", BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.author").value("Updated Author"));

        verify(bookService, times(1)).updateBook(eq(BOOK_ID), any(Book.class));
    }

    @Test
    void deleteBook_ShouldReturnNoContent() throws Exception {
        doNothing().when(bookService).deleteBook(BOOK_ID);

        mockMvc.perform(delete("/books/{id}", BOOK_ID))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(BOOK_ID);
    }

}
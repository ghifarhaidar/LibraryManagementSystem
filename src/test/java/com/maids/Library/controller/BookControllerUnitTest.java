package com.maids.Library.controller;

import com.maids.Library.exception.ResourceNotFoundException;
import com.maids.Library.model.Book;
import com.maids.Library.service.BookService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookController.class)
class BookControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        // Initialize test data
        book1 = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "978-1-45678-123-4");
        book2 = new Book(2L, "1984", "George Orwell", 1949, "978-0-596-52068-7");
    }


    @Test
    @DisplayName("GET /api/books - Should return all books")
    void testGetAllBooks() throws Exception {
        List<Book> books = Arrays.asList(book1, book2);
        Mockito.when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET /api/books/{id} - Should return book with valid ID")
    void testGetBookById_ValidBookId() throws Exception {
        Mockito.when(bookService.findById(1L)).thenReturn(book1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title").value("The Great Gatsby"));
    }

    @Test
    @DisplayName("GET /api/books/{id} - Should return 404 for invalid ID")
    void testGetBookById_InvalidBookId() throws Exception {
        Mockito.when(bookService.findById(1L)).thenThrow(new ResourceNotFoundException("Book Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Book Not Found"));
    }

    @Test
    @DisplayName("POST /api/books - Should create a new book")
    void testCreateBook_ValidBook() throws Exception {
        Mockito.when(bookService.save(Mockito.any(Book.class))).thenReturn(book1);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book1)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.title").value("The Great Gatsby"));
    }

    @Test
    @DisplayName("POST /api/books - Should return 400 for invalid title")
    void testCreateBook_InvalidBookTitle() throws Exception {
        Book book = new Book(1L, StringUtils.repeat("*", 300), "F. Scott Fitzgerald", 1925, "978-1-45678-123-4");
        Mockito.when(bookService.save(Mockito.any(Book.class))).thenReturn(book);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title must be between 1 and 255 characters"));
    }

    @Test
    @DisplayName("POST /api/books - Should return 400 for invalid title")
    void testCreateBook_NullBookTitle() throws Exception {
        Book book = new Book(1L, null, "F. Scott Fitzgerald", 1925, "978-1-45678-123-4");
        Mockito.when(bookService.save(Mockito.any(Book.class))).thenReturn(book);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is mandatory"));
    }

    @Test
    @DisplayName("POST /api/books - Should return 400 for invalid author")
    void testCreateBook_InvalidBookAuthor() throws Exception {
        Book book = new Book(1L, "The Great Gatsby", StringUtils.repeat("*", 300), 1925, "978-1-45678-123-4");
        Mockito.when(bookService.save(Mockito.any(Book.class))).thenReturn(book);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.author").value("Author must be between 1 and 255 characters"));
    }

    @Test
    @DisplayName("POST /api/books - Should return 400 for invalid author")
    void testCreateBook_NullBookAuthor() throws Exception {
        Book book = new Book(1L, "The Great Gatsby", null, 1925, "978-1-45678-123-4");
        Mockito.when(bookService.save(Mockito.any(Book.class))).thenReturn(book);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.author").value("Author is mandatory"));
    }

    @Test
    @DisplayName("POST /api/books - Should return 400 for invalid publication year")
    void testCreateBook_NullBookPublicationYear() throws Exception {
        Book book = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", null, "978-1-45678-123-4");
        Mockito.when(bookService.save(Mockito.any(Book.class))).thenReturn(book);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.publicationYear").value("Publication year is mandatory"));
    }

    @Test
    @DisplayName("POST /api/books - Should return 400 for invalid ISBN")
    void testCreateBook_InvalidBookISBN() throws Exception {
        Book book = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "978-1-45678- 123-4");
        Mockito.when(bookService.save(Mockito.any(Book.class))).thenReturn(book);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.isbn").value("Invalid ISBN format"));
    }

    @Test
    @DisplayName("POST /api/books - Should return 400 for invalid ISBN")
    void testCreateBook_NullBookISBN() throws Exception {
        Book book = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, null);
        Mockito.when(bookService.save(Mockito.any(Book.class))).thenReturn(book);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.isbn").value("ISBN is mandatory"));
    }

    @Test
    @DisplayName("PUT /api/books/{id} - Should return updated book")
    void testUpdateBook_ValidBook() throws Exception {
        Mockito.when(bookService.update(Mockito.any(Long.class),Mockito.any(Book.class))).thenReturn(book1);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title").value("The Great Gatsby"));
    }

    @Test
    @DisplayName("PUT /api/books/{id} - Should return 404 for invalid ID")
    void testUpdateBook_InvalidBookId() throws Exception {
        Mockito.when(bookService.update(Mockito.any(Long.class),Mockito.any(Book.class))).thenThrow(new ResourceNotFoundException("Book Not Found"));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book1)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Book Not Found"));
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - Should delete a book and return 204 No Content")
    void testDeleteBook_ValidBookId() throws Exception {
        doNothing().when(bookService).delete(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(bookService, Mockito.times(1)).delete(1L);

    }

    @Test
    @DisplayName("DELETE /api/books/{id} - Should return 404 for invalid book ID")
    void testDeleteBook_InvalidBookId() throws Exception {
        doThrow(new ResourceNotFoundException("Book Not Found"))
                .when(bookService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Book Not Found"));

        Mockito.verify(bookService, Mockito.times(1)).delete(1L);
    }
}
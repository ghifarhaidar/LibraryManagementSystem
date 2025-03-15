package com.maids.Library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maids.Library.exception.BookNotAvailableException;
import com.maids.Library.exception.ResourceNotFoundException;
import com.maids.Library.model.Book;
import com.maids.Library.model.BorrowingRecord;
import com.maids.Library.model.Patron;
import com.maids.Library.service.BorrowingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BorrowingController.class)
class BorrowingControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BorrowingService borrowingService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book;
    private Patron patron;
    private BorrowingRecord borrowingRecord;

    private BorrowingRecord borrowingRecord1;
    private BorrowingRecord borrowingRecord2;

    @BeforeEach
    public void setUp() {
        book = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "978-1-45678-123-4");
        patron = new Patron(1L, "Ahmad Ahmad", "Ahmad@gmail.com");
        borrowingRecord = new BorrowingRecord(book, patron, LocalDate.now());
        borrowingRecord1= new BorrowingRecord(book, patron, LocalDate.now(), LocalDate.now());
        borrowingRecord2= new BorrowingRecord(book, patron, LocalDate.now(), LocalDate.now());
    }

    @Test
    @DisplayName("GET /api/borrowingRecords - Should return all borrowingRecords")
    void testGetAllBorrowingRecords() throws Exception {
        List<BorrowingRecord> borrowingRecords = Arrays.asList(borrowingRecord1, borrowingRecord2);
        Mockito.when(borrowingService.findAll()).thenReturn(borrowingRecords);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/borrowingRecords"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("POST /api/borrow/{bookId}/patron/{patronId} - Should borrow a book")
    void testBorrowBook_ValidRequest() throws Exception {
        Mockito.when(borrowingService.borrowBook(1L, 1L)).thenReturn(borrowingRecord);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/borrow/1/patron/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.book.title").value("The Great Gatsby"))
                .andExpect(jsonPath("$.patron.name").value("Ahmad Ahmad"));
    }

    @Test
    @DisplayName("POST /api/borrow/{bookId}/patron/{patronId} - Should return 404 for invalid book ID")
    void testBorrowBook_InvalidBookId() throws Exception {
        Mockito.when(borrowingService.borrowBook(1L, 1L))
                .thenThrow(new ResourceNotFoundException("Book Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/borrow/1/patron/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book Not Found"));
    }

    @Test
    @DisplayName("POST /api/borrow/{bookId}/patron/{patronId} - Should return 404 for invalid patron ID")
    void testBorrowBook_InvalidPatronId() throws Exception {
        Mockito.when(borrowingService.borrowBook(1L, 1L))
                .thenThrow(new ResourceNotFoundException("Patron Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/borrow/1/patron/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Patron Not Found"));
    }

    @Test
    @DisplayName("POST /api/borrow/{bookId}/patron/{patronId} - Should return 409 if book is already borrowed")
    void testBorrowBook_BookAlreadyBorrowed() throws Exception {
        Mockito.when(borrowingService.borrowBook(1L, 1L))
                .thenThrow(new BookNotAvailableException());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/borrow/1/patron/1"))
                .andExpect(status().isConflict())
                .andExpect(content().string("The required book is not available!"));
    }

    @Test
    @DisplayName("PUT /api/return/{bookId}/patron/{patronId} - Should return 200 for book returned")
    void testReturnBook_ValidRequest() throws Exception {
        borrowingRecord.setReturnDate(LocalDate.now());
        Mockito.when(borrowingService.returnBook(1L, 1L)).thenReturn(borrowingRecord);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/return/1/patron/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book.title").value("The Great Gatsby"))
                .andExpect(jsonPath("$.patron.name").value("Ahmad Ahmad"))
                .andExpect(jsonPath("$.returnDate").exists());
    }

    @Test
    @DisplayName("PUT /api/return/{bookId}/patron/{patronId} - Should return 404 for invalid book ID")
    void testReturnBook_InvalidBookId() throws Exception {
        Mockito.when(borrowingService.returnBook(1L, 1L))
                .thenThrow(new ResourceNotFoundException("Book Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/return/1/patron/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book Not Found"));
    }

    @Test
    @DisplayName("PUT /api/return/{bookId}/patron/{patronId} - Should return 404 for invalid patron ID")
    void testReturnBook_InvalidPatronId() throws Exception {
        Mockito.when(borrowingService.returnBook(1L, 1L))
                .thenThrow(new ResourceNotFoundException("Patron Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/return/1/patron/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Patron Not Found"));
    }

    @Test
    @DisplayName("PUT /api/return/{bookId}/patron/{patronId} - Should return 404 if book is not borrowed")
    void testReturnBook_BookNotBorrowed() throws Exception {
        Mockito.when(borrowingService.returnBook(1L, 1L))
                .thenThrow(new ResourceNotFoundException("No active borrowing record found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/return/1/patron/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No active borrowing record found"));
    }


}
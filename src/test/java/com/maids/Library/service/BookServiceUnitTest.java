package com.maids.Library.service;

import com.maids.Library.model.Book;
import com.maids.Library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceUnitTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        book1 = new Book( "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780743273565");
        book2 = new Book( "1984", "George Orwell", 1949, "9780451524935");
    }

    @Test
    void testFindAllBooks() {
        // Mock repository behavior
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        // Call service method
        List<Book> books = bookService.findAll();

        // Verify results
        assertEquals(2, books.size());
        assertEquals("The Great Gatsby", books.get(0).getTitle());
        assertEquals("1984", books.get(1).getTitle());

        // Verify repository method was called
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void findById() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}
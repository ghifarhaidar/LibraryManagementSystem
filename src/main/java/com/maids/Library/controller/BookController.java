package com.maids.Library.controller;

import com.maids.Library.exception.ResourceNotFoundException;
import com.maids.Library.model.Book;
import com.maids.Library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // GET /api/books: Retrieve a list of all books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    // GET /api/books/{id}: Retrieve details of a specific book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookService.findById(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/books: Add a new book to the library
    @PostMapping 
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(book));
    }

    // PUT /api/books/{id}: Update an existing book's information
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id,@Valid @RequestBody Book bookDetails) {
        try {
            return ResponseEntity.ok(bookService.update(id, bookDetails));
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // DELETE /api/books/{id}: Remove a book from the library
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
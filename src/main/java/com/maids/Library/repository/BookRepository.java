package com.maids.Library.repository;

import com.maids.Library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Custom query to find a book by ISBN
    Optional<Book> findByIsbn(String isbn);
}

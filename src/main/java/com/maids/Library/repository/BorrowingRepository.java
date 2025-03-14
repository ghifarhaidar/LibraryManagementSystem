package com.maids.Library.repository;

import com.maids.Library.model.Book;
import com.maids.Library.model.BorrowingRecord;
import com.maids.Library.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowingRepository extends JpaRepository<BorrowingRecord, Long> {
    // Find an active borrowing record for a book
    Optional<BorrowingRecord> findByBookAndReturnDateIsNull(Book book);

    // Find an active borrowing record for a book and patron
    Optional<BorrowingRecord> findByBookAndPatronAndReturnDateIsNull(Book book, Patron patron);
}

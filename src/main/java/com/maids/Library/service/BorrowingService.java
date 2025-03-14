package com.maids.Library.service;

import com.maids.Library.exception.BookNotAvailableException;
import com.maids.Library.exception.ResourceNotFoundException;
import com.maids.Library.model.Book;
import com.maids.Library.model.Patron;
import com.maids.Library.model.BorrowingRecord;
import com.maids.Library.repository.BorrowingRepository;
import com.maids.Library.repository.BookRepository;
import com.maids.Library.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowingService {

    @Autowired
    private BorrowingRepository borrowingRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PatronRepository patronRepository;

    public List<BorrowingRecord> findAll() {
        return borrowingRepository.findAll();
    }
    public BorrowingRecord borrowBook(Long bookId,Long patronId) throws ResourceNotFoundException, BookNotAvailableException {
        Book book= bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        if(borrowingRepository.findByBookAndReturnDateIsNull(book).isPresent()){
            throw new BookNotAvailableException();
        }

        Patron patron= patronRepository.findById(patronId).orElseThrow(() -> new ResourceNotFoundException("Patron not found"));

        BorrowingRecord record = new BorrowingRecord(book,patron,LocalDate.now());
        return borrowingRepository.save(record);

    }

    public BorrowingRecord returnBook(Long bookId,Long patronId) throws ResourceNotFoundException {
        Book book= bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        Patron patron= patronRepository.findById(patronId).orElseThrow(() -> new ResourceNotFoundException("Patron not found"));

        BorrowingRecord record = borrowingRepository.findByBookAndPatronAndReturnDateIsNull(book, patron)
                .orElseThrow(() -> new ResourceNotFoundException("No active borrowing record found"));

        record.setReturnDate(LocalDate.now());
        return borrowingRepository.save(record);
    }

}

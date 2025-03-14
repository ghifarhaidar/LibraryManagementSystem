package com.maids.Library.controller;

import com.maids.Library.exception.BookNotAvailableException;
import com.maids.Library.exception.ResourceNotFoundException;
import com.maids.Library.model.BorrowingRecord;
import com.maids.Library.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    // GET /api/borrowingRecords: Retrieve a list of all borrowing records
    @GetMapping("/borrowingRecords")
    public ResponseEntity<List<BorrowingRecord>> getAllBorrowingRecords(){
        return ResponseEntity.ok(borrowingService.findAll());
    }

    // POST /api/borrow/{bookId}/patron/{patronId}: Allow a patron to borrow a book
    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecord> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) throws ResourceNotFoundException, BookNotAvailableException {
        return new ResponseEntity<>(borrowingService.borrowBook(bookId,patronId), HttpStatus.CREATED);
    }

    // PUT /api/return/{bookId}/patron/{patronId}: Record the return of a borrowed book by a patron
    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecord> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) throws ResourceNotFoundException {
        return new ResponseEntity<>(borrowingService.returnBook(bookId,patronId), HttpStatus.OK);
    }
}

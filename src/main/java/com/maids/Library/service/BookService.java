package com.maids.Library.service;

import com.maids.Library.exception.ResourceNotFoundException;
import com.maids.Library.model.Book;
import com.maids.Library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book findById(Long id) throws ResourceNotFoundException {
        return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book Not Found"));
    }

    @Transactional
    public Book save(Book bookDetails) {
        Book book = new Book(bookDetails.getTitle(),bookDetails.getAuthor(),bookDetails.getPublicationYear(),bookDetails.getIsbn());
        return bookRepository.save(book);
    }

    @Transactional
    public Book update(Long id, Book bookDetails) throws ResourceNotFoundException {
        Book book = findById(id);
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setIsbn(bookDetails.getIsbn());
        return bookRepository.save(book);
    }

    @Transactional
    public void delete(Long id) throws ResourceNotFoundException {
        bookRepository.delete(findById(id));
    }
}
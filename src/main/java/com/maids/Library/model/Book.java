package com.maids.Library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;


@Entity
@Table(name = "books")
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Author is mandatory")
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
    private String author;

    @NotNull(message = "Publication year is mandatory")
    @Min(value = 1000, message = "Publication year must be after 1000")
    private Integer publicationYear;

    @NotBlank(message = "ISBN is mandatory")
    @Column(unique = true)
    @Pattern(regexp = "^(?=(?:[^0-9]*[0-9]){10}(?:(?:[^0-9]*[0-9]){3})?$)[\\d-]+$", message = "Invalid ISBN format")
    @NotNull
    private String isbn;

    @Autowired
    public Book(Long id, String title, String author, Integer publicationYear, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
    }

    public Book(String title, String author, Integer publicationYear, String isbn) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
    }

    public Long getId() {
        return id;
    }

    public @NotBlank(message = "Title is mandatory") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title is mandatory") String title) {
        this.title = title;
    }

    public @NotBlank(message = "Author is mandatory") String getAuthor() {
        return author;
    }

    public void setAuthor(@NotBlank(message = "Author is mandatory") String author) {
        this.author = author;
    }

    public @NotNull(message = "Publication year is mandatory") Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(@NotNull(message = "Publication year is mandatory") Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public @NotBlank(message = "ISBN is mandatory") String getIsbn() {
        return isbn;
    }

    public void setIsbn(@NotBlank(message = "ISBN is mandatory") String isbn) {
        this.isbn = isbn;
    }
}
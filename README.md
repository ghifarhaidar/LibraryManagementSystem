# Library Management System

## Description
A Spring Boot application for managing books, patrons, and borrowing records. 

The system allows librarians to:
- Add, update, and delete books.
- Add, update, and delete patrons.
- Track borrowing and returning of books.

## Table of Contents
1. [How to Run](#how-to-run)
2. [API Endpoints](#api-endpoints)
    - [Books](#books)
    - [Patrons](#patrons)
    - [Borrowing](#borrowing)
3. [Testing](#testing)
4. [Database Configuration](#database-configuration)
    - [MySQL](#mysql)
5. [Swagger](#swagger)
---

## How to Run

### Prerequisites
- Java 21 or higher
- Maven 3.x
- MySQL (or another database of your choice)
- IDE (e.g., IntelliJ IDEA, Eclipse, or VS Code)

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/ghifarhaidar/LibraryManagementSystem.git
   cd LibraryManagementSystem

2. Configure the Database:

    - Update the application.properties file with your database credentials:
    
    ```bash 
    spring.datasource.url=jdbc:mysql://localhost:3306/librarydb
    spring.datasource.username=root
    spring.datasource.password=yourpassword
    spring.jpa.hibernate.ddl-auto=update
   
3. Build the Application:
    ```bash
    mvn clean install

4. Run the Application:
    ```bash
    mvn spring-boot:run
   
5. Access the Application:
    - The application will start on http://localhost:8080.

## API Endpoints
### Base URL
    http://localhost:8080/api

### Books
- GET /books: Retrieve all books.
    ```bash
    curl -X GET http://localhost:8080/api/books
  
- GET /books/{id}: Retrieve a specific book by ID.
    ```bash
    curl -X GET http://localhost:8080/api/books/1
  
- POST /books: Add a new book.
    ```bash
    curl -X POST http://localhost:8080/api/books \
    -H "Content-Type: application/json" \
    -d '{
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "publicationYear": 1925,
    "isbn": "978-1-45678-123-4"
    }'
  
- PUT /books/{id}: Update an existing book.
    ```bash
    curl -X PUT http://localhost:8080/api/books/1 \
    -H "Content-Type: application/json" \
    -d '{
    "title": "1984",
    "author": "George Orwell",
    "publicationYear": 1949,
    "isbn": "978-0-596-52068-7"
    }'
  
- DELETE /books/{id}: Delete a book.
    ```bash
    curl -X DELETE http://localhost:8080/api/books/1

### Patrons
- GET /patrons: Retrieve all patrons.
    ```bash
    curl -X GET http://localhost:8080/api/patrons
  
- GET /patrons/{id}: Retrieve a specific patron by ID.
    ```bash
    curl -X GET http://localhost:8080/api/patrons/1
  
- POST /patrons: Add a new patron.
    ```bash
    curl -X POST http://localhost:8080/api/patrons \
    -H "Content-Type: application/json" \
    -d '{
    "name": "Ahmad Ahmad",
    "contactInformation": "Ahmad@gamil.com"
    }'
  
- PUT /patrons/{id}: Update an existing patron.
    ```bash
    curl -X PUT http://localhost:8080/api/patrons/1 \
    -H "Content-Type: application/json" \
    -d '{
    "name": "Mohammad Ahmad",
    "contactInformation": "Mohammad@gamil.com"
    }'
  
- DELETE /patrons/{id}: Delete a patron.
    ```bash
    curl -X DELETE http://localhost:8080/api/patrons/1

### Borrowing
- GET /api/borrowingRecords: Retrieve all borrowing records.
    ```bash
    curl -X GET http://localhost:8080/api/borrowingRecords

- POST /borrow/{bookId}/patron/{patronId}: Borrow a book.
    ```bash
    curl -X POST http://localhost:8080/api/borrow/1/patron/1
  
- PUT /return/{bookId}/patron/{patronId}: Return a borrowed book.
    ```bash
    curl -X PUT http://localhost:8080/api/return/1/patron/1
  
## Testing

### Running Unit Tests
- Run all unit tests using Maven:
    ```bash
    mvn test

## Database Configuration

### MySQL

1. Create a database named librarydb:
    ```bash
   CREATE DATABASE librarydb;

2. Update the application.properties file with your MySQL credentials:
    ```bash 
    spring.datasource.url=jdbc:mysql://localhost:3306/librarydb
    spring.datasource.username=root
    spring.datasource.password=yourpassword
    spring.jpa.hibernate.ddl-auto=update
   
## Dependencies

### Maven Dependencies

- Spring Boot Starter

- Spring Boot Starter Test

- Spring Boot Starter Web

- Spring Boot Devtools

- Spring Boot Starter Data JPA

- MySQL Connector j

- Spring Boot Starter Validation

- Lombok

- Springdoc Openapi Starter Webmvc Ui

### Adding Dependencies
- Update your pom.xml file with the required dependencies.

## Swagger
- Access the Swagger UI at
    ```
  http://localhost:8080/swagger-ui/index.html#/
package com.maids.Library.exception;

public class BookNotAvailableException extends Exception {

    public BookNotAvailableException() {
        super("The required book is not available!");
    }
}

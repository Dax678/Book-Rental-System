package org.bookrental.bookrentalsystem.Config.Exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }
}

package org.bookrental.bookrentalsystem.Config.Exception;

public class RentalNotFoundException extends RuntimeException {
    public RentalNotFoundException(String message) {
        super(message);
    }
}

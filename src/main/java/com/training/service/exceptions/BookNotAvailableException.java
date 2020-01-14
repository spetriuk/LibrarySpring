package com.training.service.exceptions;

public class BookNotAvailableException extends Throwable {
    @Override
    public String getMessage() {
        return "The book is not available";
    }
}

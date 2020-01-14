package com.training.service.exceptions;

public class AuthorNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "Author not found";
    }
}

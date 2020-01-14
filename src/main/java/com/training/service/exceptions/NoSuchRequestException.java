package com.training.service.exceptions;

public class NoSuchRequestException extends Exception {
    @Override
    public String getMessage() {
        return "No such request";
    }
}

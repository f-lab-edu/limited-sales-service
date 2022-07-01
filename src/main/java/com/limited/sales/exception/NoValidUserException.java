package com.limited.sales.exception;

public class NoValidUserException extends RuntimeException{
    public NoValidUserException(String message) {
        super(message);
    }
}

package com.limited.sales.exception.sub;

public class NoValidUserException extends RuntimeException{
    public NoValidUserException(String message) {
        super(message);
    }
}

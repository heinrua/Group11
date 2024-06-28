package com.nhom11.englishapp.exception;

public class NotEnoughItemsException extends RuntimeException {
    public NotEnoughItemsException(int current, int min) {
        super("Expected minimum size of " + min + ", got " + current);
    }

    public NotEnoughItemsException(String message) {
        super(message);
    }
}

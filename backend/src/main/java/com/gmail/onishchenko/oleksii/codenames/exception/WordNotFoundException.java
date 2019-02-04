package com.gmail.onishchenko.oleksii.codenames.exception;

public class WordNotFoundException extends CodenamesException {

    private static final long serialVersionUID = 2328166431018011127L;

    public WordNotFoundException(String message) {
        super(message);
    }
}

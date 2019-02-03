package com.gmail.onishchenko.oleksii.codenames.exception;

public class CodenamesException extends RuntimeException {

    private static final long serialVersionUID = -5880382013507330425L;

    public CodenamesException() {
    }

    public CodenamesException(String message) {
        super(message);
    }
}

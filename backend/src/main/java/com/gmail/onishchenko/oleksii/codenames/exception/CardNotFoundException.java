package com.gmail.onishchenko.oleksii.codenames.exception;

public class CardNotFoundException extends CodenamesException {

    private static final long serialVersionUID = -6772729648101611148L;

    public CardNotFoundException(String message) {
        super(message);
    }
}

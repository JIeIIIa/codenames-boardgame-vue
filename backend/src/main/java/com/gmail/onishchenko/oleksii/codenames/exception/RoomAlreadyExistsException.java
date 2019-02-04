package com.gmail.onishchenko.oleksii.codenames.exception;

public class RoomAlreadyExistsException extends CreateRoomException {

    private static final long serialVersionUID = 1699288871638424117L;

    public RoomAlreadyExistsException(String message) {
        super(message);
    }
}

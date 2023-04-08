package ru.clevertec.ecl.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "User was not found";
    private static final String DEFAULT_MESSAGE_WITH_ID = "User with ID %s was not found";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public UserNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE_WITH_ID, id));
    }
}

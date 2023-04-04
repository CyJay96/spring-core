package ru.clevertec.ecl.exception;

public class TagNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE_WITH_ID = "Tag with ID %s was not found";

    public TagNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE_WITH_ID, id));
    }
}

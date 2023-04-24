package ru.clevertec.ecl.exception;

public class OrderNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE_WITH_ID = "Order with ID %s was not found";

    public OrderNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE_WITH_ID, id));
    }
}

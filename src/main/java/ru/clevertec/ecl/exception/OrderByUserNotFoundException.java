package ru.clevertec.ecl.exception;

public class OrderByUserNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE_WITH_ID = "Order with ID %s by User with ID %s was not found";

    public OrderByUserNotFoundException(Long orderId, Long userId) {
        super(String.format(DEFAULT_MESSAGE_WITH_ID, orderId, userId));
    }
}

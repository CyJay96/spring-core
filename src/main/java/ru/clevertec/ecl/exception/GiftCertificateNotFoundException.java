package ru.clevertec.ecl.exception;

public class GiftCertificateNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE_WITH_ID = "Gift Certificate with ID %s was not found";

    public GiftCertificateNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE_WITH_ID, id));
    }
}

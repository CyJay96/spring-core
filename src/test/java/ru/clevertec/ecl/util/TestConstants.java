package ru.clevertec.ecl.util;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class TestConstants {

    public static final String CREATE_DB = "create-db.sql";
    public static final String INIT_DB = "init-db.sql";

    public static final Integer PAGE = 1;
    public static final Integer PAGE_SIZE = 18;

    public static final Long TEST_ID = 1L;
    public static final Long TEST_NUMBER = 1L;
    public static final String TEST_STRING = "test_string";
    public static final BigDecimal TEST_BIG_DECIMAL = BigDecimal.ONE;
    public static final BigDecimal TEST_BIG_DECIMAL_ZERO = BigDecimal.ZERO;
    public static final boolean TEST_BOOLEAN = true;
    public static final OffsetDateTime TEST_DATE = OffsetDateTime.now();
}

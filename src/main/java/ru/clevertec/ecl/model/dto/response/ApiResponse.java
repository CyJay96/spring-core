package ru.clevertec.ecl.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ApiResponse<T> {

//    private final OffsetDateTime timestamp = OffsetDateTime.now();

    private final int status;

    private final String message;

    private final String path;

    private final String color;

    private final T data;

    @Getter
    @RequiredArgsConstructor
    public enum Color {
        SUCCESS("success", "green"),
        DANGER("danger", "red"),
        WARNING("warning", "yellow"),
        INFO("info", "blue"),
        DEFAULT_DARK("default_dark", "dark"),
        DEFAULT_LIGHT("default light", "light");

        private final String value;

        private final String color;
    }

    public static <T> ResponseEntity<ApiResponse<T>> apiResponseEntity (
            final String message,
            final String path,
            final HttpStatus httpStatus,
            final ApiResponse.Color color,
            final T body
    ) {
        final ApiResponse<T> apiResponse = ApiResponse.<T>builder()
                .message(message)
                .path(path)
                .status(httpStatus.value())
                .color(color.getColor())
                .data(body)
                .build();

        return new ResponseEntity<>(apiResponse, httpStatus);
    }
}

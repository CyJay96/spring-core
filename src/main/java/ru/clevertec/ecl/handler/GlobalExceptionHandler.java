package ru.clevertec.ecl.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.OrderByUserNotFoundException;
import ru.clevertec.ecl.model.dto.response.ApiResponse;

import java.util.Optional;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        final String errorMessage = exception.getBindingResult().getAllErrors().stream()
                .map(error ->
                        String.format("%s: %s", ((FieldError) error).getField(), error.getDefaultMessage()))
                .reduce((a, b) -> a + "; " + b)
                .orElse("Undefined error message");

        log.warn(exception.getMessage(), exception);

        return generateErrorResponse(exception, HttpStatus.BAD_REQUEST, request, errorMessage);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFoundException(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        log.warn(exception.getMessage(), exception);

        return generateErrorResponse(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(OrderByUserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleOrderByUserNotFoundException(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        log.warn(exception.getMessage(), exception);
        return generateErrorResponse(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleServerSideErrorException(
            Exception exception,
            HttpServletRequest request
    ) {
        log.error(exception.getMessage(), exception);
        return generateErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ApiResponse<Void>> generateErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            HttpServletRequest request
    ) {
        final ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                .status(httpStatus.value())
                .message(exception.getMessage())
                .path(request.getServletPath())
                .data(null)
                .build();

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    private ResponseEntity<ApiResponse<Void>> generateErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            HttpServletRequest request,
            String customMessage
    ) {
        final ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                .status(httpStatus.value())
                .message(Optional.ofNullable(customMessage).orElse(exception.getMessage()))
                .path(request.getServletPath())
                .data(null)
                .build();

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}

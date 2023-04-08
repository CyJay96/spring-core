package ru.clevertec.ecl.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.exception.OrderNotFoundException;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.model.dto.response.ApiResponse;

import java.util.Optional;

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

        return generateErrorResponse(exception, HttpStatus.BAD_REQUEST, request, errorMessage, ApiResponse.Color.DANGER);
    }

    @ExceptionHandler(GiftCertificateNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleGiftCertificateNotFoundException(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        return generateErrorResponse(exception, HttpStatus.NOT_FOUND, request, ApiResponse.Color.WARNING);
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTagNotFoundException(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        return generateErrorResponse(exception, HttpStatus.NOT_FOUND, request, ApiResponse.Color.WARNING);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        return generateErrorResponse(exception, HttpStatus.NOT_FOUND, request, ApiResponse.Color.WARNING);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleOrderNotFoundException(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        return generateErrorResponse(exception, HttpStatus.NOT_FOUND, request, ApiResponse.Color.WARNING);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleServerSideErrorException(
            Exception exception,
            HttpServletRequest request
    ) {
        return generateErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR, request, ApiResponse.Color.DANGER);
    }

    private ResponseEntity<ApiResponse<Void>> generateErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            HttpServletRequest request,
            ApiResponse.Color errorColor
    ) {
        final ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                .status(httpStatus.value())
                .message(exception.getMessage())
                .path(request.getServletPath())
                .color(errorColor.getValue())
                .data(null)
                .build();

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    private ResponseEntity<ApiResponse<Void>> generateErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            HttpServletRequest request,
            String customMessage,
            ApiResponse.Color errorColor
    ) {
        final ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                .status(httpStatus.value())
                .message(Optional.ofNullable(customMessage).orElse(exception.getMessage()))
                .path(request.getServletPath())
                .color(errorColor.getValue())
                .data(null)
                .build();

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}

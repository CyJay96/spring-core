package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.config.PaginationProperties;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.exception.OrderNotFoundException;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.model.dto.response.ApiResponse;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.service.OrderService;

import java.util.Optional;

import static ru.clevertec.ecl.controller.OrderController.ORDER_API_PATH;
import static ru.clevertec.ecl.model.dto.response.ApiResponse.apiResponseEntity;

/**
 * Order API
 *
 * @author Konstantin Voytko
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ORDER_API_PATH)
public class OrderController {

    private final OrderService orderService;
    private final PaginationProperties paginationProperties;

    public static final String ORDER_API_PATH = "/api/v0/orders";

    /**
     * POST /api/v0/orders/{userId}/{giftCertificateId} : Create a new Order
     *
     * @param userId User ID to create Order (required)
     * @param giftCertificateId Gift Certificate ID to create Order (required)
     * @throws UserNotFoundException if the User with ID doesn't exist
     * @throws GiftCertificateNotFoundException if the Gift Certificate with ID doesn't exist
     */
    @PostMapping("/{userId}/{giftCertificateId}")
    public ResponseEntity<ApiResponse<OrderDtoResponse>> createOrderByUserIdAndGiftCertificateId(
            @PathVariable @Valid @NotNull Long userId,
            @PathVariable @Valid @NotNull Long giftCertificateId
    ) {
        OrderDtoResponse orderDtoResponse = orderService.createOrderByUserIdAndGiftCertificateId(userId, giftCertificateId);

        return apiResponseEntity(
                "Order with ID " + orderDtoResponse.getId() + " was created by " +
                        "user_id: " + userId + "; gift_certificate_id: " + giftCertificateId,
                ORDER_API_PATH,
                HttpStatus.CREATED,
                ApiResponse.Color.SUCCESS,
                orderDtoResponse
        );
    }

    /**
     * GET /api/v0/orders : Find Orders info
     *
     * @param page page value to return (not required)
     * @param pageSize page size to return (not required)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<OrderDtoResponse>>> findAllOrders(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        page = Optional.ofNullable(page).orElse(paginationProperties.getDefaultPageValue());
        pageSize = Optional.ofNullable(pageSize).orElse(paginationProperties.getDefaultPageSize());

        PageResponse<OrderDtoResponse> orders = orderService.getAllOrders(page, pageSize);

        return apiResponseEntity(
                "All Orders: " +
                        "; page: " + page +
                        "; page_size: " + pageSize,
                ORDER_API_PATH,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                orders
        );
    }

    /**
     * GET /api/v0/orders/{userId} : Find Orders info by User ID
     *
     * @param userId User ID to return Order (required)
     * @param page page value to return (not required)
     * @param pageSize page size to return (not required)
     */
    @GetMapping("/byUserId/{userId}")
    public ResponseEntity<ApiResponse<PageResponse<OrderDtoResponse>>> findAllOrdersByUserId(
            @PathVariable @Valid @NotNull Long userId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        page = Optional.ofNullable(page).orElse(paginationProperties.getDefaultPageValue());
        pageSize = Optional.ofNullable(pageSize).orElse(paginationProperties.getDefaultPageSize());

        PageResponse<OrderDtoResponse> orders = orderService.getAllOrdersByUserId(userId, page, pageSize);

        return apiResponseEntity(
                "All Orders by User ID: " + userId +
                        "; page: " + page +
                        "; page_size: " + pageSize,
                ORDER_API_PATH,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                orders
        );
    }

    /**
     * GET /api/v0/Orders/{id} : Find Order info
     *
     * @param id Order ID to return (required)
     * @throws OrderNotFoundException if the Order with ID doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDtoResponse>> findOrderById(@PathVariable @Valid @NotNull Long id) {
        OrderDtoResponse user = orderService.getOrderById(id);

        return apiResponseEntity(
                "Order with ID " + user.getId() + " was found",
                ORDER_API_PATH + "/" + id,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                user
        );
    }

    /**
     * GET /api/v0/Orders/{orderId}/{userId} : Find Order info by ID and User ID
     *
     * @param orderId Order ID to return (required)
     * @param userId User ID to return (required)
     * @throws OrderNotFoundException if the Order with ID doesn't exist
     * @throws UserNotFoundException if the User with ID doesn't exist
     */
    @GetMapping("/{orderId}/{userId}")
    public ResponseEntity<ApiResponse<OrderDtoResponse>> findOrderByIdAndUserId(
            @PathVariable @Valid @NotNull Long orderId,
            @PathVariable @Valid @NotNull Long userId
    ) {
        OrderDtoResponse user = orderService.getOrderByIdAndUserId(orderId, userId);

        return apiResponseEntity(
                "Order with ID " + user.getId() + " and User ID " + userId + " was found",
                ORDER_API_PATH + "/" + orderId + "/" + userId,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                user
        );
    }
}

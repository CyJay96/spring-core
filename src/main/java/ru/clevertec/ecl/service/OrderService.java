package ru.clevertec.ecl.service;

import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;

public interface OrderService {

    OrderDtoResponse createOrderByUserIdAndGiftCertificateId(Long userId, Long giftCertificateId);

    PageResponse<OrderDtoResponse> getAllOrders(Integer page, Integer pageSize);

    PageResponse<OrderDtoResponse> getAllOrdersByUserId(Long userId, Integer page, Integer pageSize);

    OrderDtoResponse getOrderById(Long id);

    OrderDtoResponse getOrderByIdAndUserId(Long orderId, Long userId);
}

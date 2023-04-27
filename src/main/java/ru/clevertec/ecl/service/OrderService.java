package ru.clevertec.ecl.service;

import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;

public interface OrderService {

    OrderDtoResponse saveByUserIdAndGiftCertificateId(Long userId, Long giftCertificateId);

    PageResponse<OrderDtoResponse> findAll(Integer page, Integer pageSize);

    PageResponse<OrderDtoResponse> findAllByUserId(Long userId, Integer page, Integer pageSize);

    OrderDtoResponse findById(Long id);

    OrderDtoResponse findByIdAndUserId(Long orderId, Long userId);
}

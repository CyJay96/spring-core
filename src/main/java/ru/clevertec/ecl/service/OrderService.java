package ru.clevertec.ecl.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;

public interface OrderService {

    OrderDtoResponse saveByUserIdAndGiftCertificateId(Long userId, Long giftCertificateId);

    PageResponse<OrderDtoResponse> findAll(Pageable pageable);

    PageResponse<OrderDtoResponse> findAllByUserId(Long userId, Pageable pageable);

    OrderDtoResponse findById(Long id);

    OrderDtoResponse findByIdAndUserId(Long orderId, Long userId);
}

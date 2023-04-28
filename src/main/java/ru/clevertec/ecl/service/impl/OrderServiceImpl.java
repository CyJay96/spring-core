package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.OrderByUserNotFoundException;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.entity.Order;
import ru.clevertec.ecl.model.entity.User;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.service.OrderService;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDtoResponse saveByUserIdAndGiftCertificateId(Long userId, Long giftCertificateId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, userId));

        GiftCertificate giftCertificate = giftCertificateRepository.findById(giftCertificateId)
                .orElseThrow(() -> new EntityNotFoundException(GiftCertificate.class, giftCertificateId));

        Order order = Order.builder()
                .user(user)
                .giftCertificate(giftCertificate)
                .finalPrice(giftCertificate.getPrice())
                .createDate(OffsetDateTime.now())
                .lastUpdateDate(OffsetDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderDtoResponse(savedOrder);
    }

    @Override
    public PageResponse<OrderDtoResponse> findAll(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<OrderDtoResponse> orderDtoResponses = orderPage.stream()
                .map(orderMapper::toOrderDtoResponse)
                .toList();

        return PageResponse.<OrderDtoResponse>builder()
                .content(orderDtoResponses)
                .number(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .numberOfElements(orderDtoResponses.size())
                .build();
    }

    @Override
    public PageResponse<OrderDtoResponse> findAllByUserId(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(User.class, userId);
        }

        Page<Order> orderPage = orderRepository.findAllByUserId(userId, pageable);

        List<OrderDtoResponse> orderDtoResponses = orderPage.stream()
                .map(orderMapper::toOrderDtoResponse)
                .toList();

        return PageResponse.<OrderDtoResponse>builder()
                .content(orderDtoResponses)
                .number(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .numberOfElements(orderDtoResponses.size())
                .build();
    }

    @Override
    public OrderDtoResponse findById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toOrderDtoResponse)
                .orElseThrow(() -> new EntityNotFoundException(Order.class, id));
    }

    @Override
    public OrderDtoResponse findByIdAndUserId(Long orderId, Long userId) {
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException(Order.class, orderId);
        }
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(User.class, userId);
        }
        return orderRepository.findByIdAndUserId(orderId, userId)
                .map(orderMapper::toOrderDtoResponse)
                .orElseThrow(() -> new OrderByUserNotFoundException(orderId, userId));
    }
}

package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.exception.OrderNotFoundException;
import ru.clevertec.ecl.exception.UserNotFoundException;
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
    public OrderDtoResponse createOrderByUserIdAndGiftCertificateId(Long userId, Long giftCertificateId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        GiftCertificate giftCertificate = giftCertificateRepository.findById(giftCertificateId)
                .orElseThrow(() -> new GiftCertificateNotFoundException(giftCertificateId));

        Order order = Order.builder()
                .user(user)
                .giftCertificate(giftCertificate)
                .finalPrice(giftCertificate.getPrice())
                .createDate(OffsetDateTime.now())
                .lastUpdateDate(OffsetDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public PageResponse<OrderDtoResponse> getAllOrders(Integer page, Integer pageSize) {
        Page<Order> orderPage = orderRepository.findAll(PageRequest.of(page, pageSize));

        List<OrderDtoResponse> orderDtoResponses = orderPage.stream()
                .map(orderMapper::toDto)
                .toList();

        return PageResponse.<OrderDtoResponse>builder()
                .content(orderDtoResponses)
                .number(page)
                .size(pageSize)
                .numberOfElements(orderDtoResponses.size())
                .build();
    }

    @Override
    public PageResponse<OrderDtoResponse> getAllOrdersByUserId(Long userId, Integer page, Integer pageSize) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        Page<Order> orderPage = orderRepository.findAllByUserId(userId, PageRequest.of(page, pageSize));

        List<OrderDtoResponse> orderDtoResponses = orderPage.stream()
                .map(orderMapper::toDto)
                .toList();

        return PageResponse.<OrderDtoResponse>builder()
                .content(orderDtoResponses)
                .number(page)
                .size(pageSize)
                .numberOfElements(orderDtoResponses.size())
                .build();
    }

    @Override
    public OrderDtoResponse getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public OrderDtoResponse getOrderByIdAndUserId(Long orderId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return orderRepository.findByIdAndUserId(orderId, userId)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}

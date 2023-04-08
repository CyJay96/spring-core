package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.exception.OrderNotFoundException;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.entity.Order;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.service.OrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

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
    public OrderDtoResponse getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}

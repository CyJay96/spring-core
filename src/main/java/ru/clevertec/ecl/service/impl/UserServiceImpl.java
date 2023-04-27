package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.model.entity.User;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public PageResponse<UserDtoResponse> findAll(Integer page, Integer pageSize) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, pageSize));

        List<UserDtoResponse> userDtoResponses = userPage.stream()
                .map(userMapper::toUserDtoResponse)
                .toList();

        return PageResponse.<UserDtoResponse>builder()
                .content(userDtoResponses)
                .number(page)
                .size(pageSize)
                .numberOfElements(userDtoResponses.size())
                .build();
    }

    @Override
    public UserDtoResponse findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDtoResponse)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    @Override
    public UserDtoResponse findByHighestOrderCost() {
        return userRepository.findByHighestOrderCost()
                .map(userMapper::toUserDtoResponse)
                .orElseThrow(() -> new EntityNotFoundException(User.class));
    }
}

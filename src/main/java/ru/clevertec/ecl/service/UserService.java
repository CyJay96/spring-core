package ru.clevertec.ecl.service;

import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.UserDtoResponse;

public interface UserService {

    PageResponse<UserDtoResponse> findAll(Integer page, Integer pageSize);

    UserDtoResponse findById(Long id);

    UserDtoResponse findByHighestOrderCost();
}

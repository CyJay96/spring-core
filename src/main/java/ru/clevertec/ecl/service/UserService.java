package ru.clevertec.ecl.service;

import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.UserDtoResponse;

public interface UserService {

    PageResponse<UserDtoResponse> getAllUsers(Integer page, Integer pageSize);

    UserDtoResponse getUserById(Long id);

    UserDtoResponse getUserByHighestOrderCost();
}

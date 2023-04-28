package ru.clevertec.ecl.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.UserDtoResponse;

public interface UserService {

    PageResponse<UserDtoResponse> findAll(Pageable pageable);

    UserDtoResponse findById(Long id);

    UserDtoResponse findByHighestOrderCost();
}

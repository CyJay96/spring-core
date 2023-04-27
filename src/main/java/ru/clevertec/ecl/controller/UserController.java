package ru.clevertec.ecl.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.config.PaginationProperties;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.dto.response.ApiResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.service.UserService;

import java.util.Optional;

import static ru.clevertec.ecl.controller.UserController.USER_API_PATH;

/**
 * User API
 *
 * @author Konstantin Voytko
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = USER_API_PATH)
public class UserController {

    private final UserService userService;
    private final PaginationProperties paginationProperties;

    public static final String USER_API_PATH = "/v0/users";

    /**
     * GET /api/v0/users : Find Users info
     *
     * @param page page value to return (not required)
     * @param pageSize page size to return (not required)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserDtoResponse>>> findAll(
            @RequestParam(value = "page", required = false) @PositiveOrZero Integer page,
            @RequestParam(value = "pageSize", required = false) @PositiveOrZero Integer pageSize) {
        page = Optional.ofNullable(page).orElse(paginationProperties.getDefaultPageValue());
        pageSize = Optional.ofNullable(pageSize).orElse(paginationProperties.getDefaultPageSize());

        PageResponse<UserDtoResponse> users = userService.findAll(page, pageSize);

        return ApiResponse.of(
                "All Users: " +
                        "; page: " + page +
                        "; page_size: " + pageSize,
                USER_API_PATH,
                HttpStatus.OK,
                users
        );
    }

    /**
     * GET /api/v0/users/{id} : Find User info
     *
     * @param id User ID to return (required)
     * @throws EntityNotFoundException if the User with ID doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDtoResponse>> findById(@PathVariable @NotNull @PositiveOrZero Long id) {
        UserDtoResponse user = userService.findById(id);

        return ApiResponse.of(
                "User with ID " + user.getId() + " was found",
                USER_API_PATH + "/" + id,
                HttpStatus.OK,
                user
        );
    }

    /**
     * GET /api/v0/users/highestOrderCost : Find User info with highest order cost
     *
     * @throws EntityNotFoundException if the User with doesn't exist
     */
    @GetMapping("/highestOrderCost")
    public ResponseEntity<ApiResponse<UserDtoResponse>> findByHighestOrderCost() {
        UserDtoResponse user = userService.findByHighestOrderCost();

        return ApiResponse.of(
                "User with highest order cost was found. ID: " + user.getId(),
                USER_API_PATH + "/highestOrderCost",
                HttpStatus.OK,
                user
        );
    }
}

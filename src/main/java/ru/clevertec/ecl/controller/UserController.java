package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.model.dto.response.ApiResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.service.UserService;

import java.util.Optional;

import static ru.clevertec.ecl.controller.UserController.USER_API_PATH;
import static ru.clevertec.ecl.model.dto.response.ApiResponse.apiResponseEntity;

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

    public static final String USER_API_PATH = "/api/v0/users";

    /**
     * GET /api/v0/users : Find Users info
     *
     * @param page page value to return (not required)
     * @param pageSize page size to return (not required)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserDtoResponse>>> findAllUsers(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        page = Optional.ofNullable(page).orElse(paginationProperties.getDefaultPageValue());
        pageSize = Optional.ofNullable(pageSize).orElse(paginationProperties.getDefaultPageSize());

        PageResponse<UserDtoResponse> users = userService.getAllUsers(page, pageSize);

        return apiResponseEntity(
                "All Users: " +
                        "; page: " + page +
                        "; page_size: " + pageSize,
                USER_API_PATH,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                users
        );
    }

    /**
     * GET /api/v0/users/{id} : Find User info
     *
     * @param id User id to return (required)
     * @throws UserNotFoundException if the User with id doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDtoResponse>> findUserById(@PathVariable @Valid @NotNull Long id) {
        UserDtoResponse user = userService.getUserById(id);

        return apiResponseEntity(
                "User with ID " + user.getId() + " was found",
                USER_API_PATH + "/" + id,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                user
        );
    }
}

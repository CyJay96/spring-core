package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.config.PaginationProperties;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.ApiResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.service.TagService;

import java.util.Optional;

import static ru.clevertec.ecl.controller.TagController.TAG_API_PATH;

/**
 * Tag API
 *
 * @author Konstantin Voytko
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = TAG_API_PATH)
public class TagController {

    private final TagService tagService;
    private final PaginationProperties paginationProperties;

    public static final String TAG_API_PATH = "/v0/tags";

    /**
     * POST /api/v0/tags : Create a new Tag
     *
     * @param tagDtoRequest Tag object to create (required)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TagDtoResponse>> save(@RequestBody @Valid TagDtoRequest tagDtoRequest) {
        TagDtoResponse tag = tagService.save(tagDtoRequest);

        return ApiResponse.of(
                "Tag with ID " + tag.getId() + " was created",
                TAG_API_PATH,
                HttpStatus.CREATED,
                tag
        );
    }

    /**
     * GET /api/v0/tags : Find Tags info
     *
     * @param page page value to return (not required)
     * @param pageSize page size to return (not required)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TagDtoResponse>>> findAll(
            @RequestParam(value = "page", required = false) @PositiveOrZero Integer page,
            @RequestParam(value = "pageSize", required = false) @PositiveOrZero Integer pageSize) {
        page = Optional.ofNullable(page).orElse(paginationProperties.getDefaultPageValue());
        pageSize = Optional.ofNullable(pageSize).orElse(paginationProperties.getDefaultPageSize());

        PageResponse<TagDtoResponse> tags = tagService.findAll(page, pageSize);

        return ApiResponse.of(
                "All Tags: " +
                        "; page: " + page +
                        "; page_size: " + pageSize,
                TAG_API_PATH,
                HttpStatus.OK,
                tags
        );
    }

    /**
     * GET /api/v0/tags/{id} : Find Tag info
     *
     * @param id Tag ID to return (required)
     * @throws EntityNotFoundException if the Tag with ID doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagDtoResponse>> findById(@PathVariable @NotNull @PositiveOrZero Long id) {
        TagDtoResponse tag = tagService.findById(id);

        return ApiResponse.of(
                "Tag with ID " + tag.getId() + " was found",
                TAG_API_PATH + "/" + id,
                HttpStatus.OK,
                tag
        );
    }

    /**
     * PUT /api/v0/tags/{id} : Update an existing Tag info
     *
     * @param id Tag ID to return (required)
     * @param tagDtoRequest Tag object to update (required)
     * @throws EntityNotFoundException if the Tag with ID doesn't exist
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<TagDtoResponse>> update(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody @Valid TagDtoRequest tagDtoRequest) {
        TagDtoResponse tag = tagService.update(id, tagDtoRequest);

        return ApiResponse.of(
                "Changes were applied to the Tag with ID " + id,
                TAG_API_PATH + "/" + id,
                HttpStatus.OK,
                tag
        );
    }

    /**
     * PATCH /api/v0/tags/{id} : Partial Update an existing Tag info
     *
     * @param id Tag ID to return (required)
     * @param tagDtoRequest Tag object to update (required)
     * @throws EntityNotFoundException if Tag with ID doesn't exist
     */
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<TagDtoResponse>> updatePartially(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody TagDtoRequest tagDtoRequest) {
        TagDtoResponse tag = tagService.updatePartially(id, tagDtoRequest);

        return ApiResponse.of(
                "Partial changes were applied to the Tag with ID " + id,
                TAG_API_PATH + "/" + id,
                HttpStatus.OK,
                tag
        );
    }

    /**
     * DELETE /api/v0/tags/{id} : Delete a Tag
     *
     * @param id Tag ID to return (required)
     * @throws EntityNotFoundException if the Tag with ID doesn't exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable @NotNull @PositiveOrZero Long id) {
        tagService.deleteById(id);

        return ApiResponse.of(
                "Tag with ID " + id + " was deleted",
                TAG_API_PATH + "/" + id,
                HttpStatus.NO_CONTENT,
                null
        );
    }
}

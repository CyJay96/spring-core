package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.ApiResponse;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.service.GiftCertificateService;

import static ru.clevertec.ecl.controller.GiftCertificateController.GIFT_CERTIFICATE_API_PATH;

/**
 * Gift Certificate API
 *
 * @author Konstantin Voytko
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = GIFT_CERTIFICATE_API_PATH)
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    public static final String GIFT_CERTIFICATE_API_PATH = "/v0/giftCertificates";

    /**
     * POST /api/v0/giftCertificates : Create a new Gift Certificate
     *
     * @param giftCertificateDtoRequest Gift Certificate object to create (required)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<GiftCertificateDtoResponse>> save(
            @RequestBody @Valid GiftCertificateDtoRequest giftCertificateDtoRequest) {
        GiftCertificateDtoResponse giftCertificate = giftCertificateService
                .save(giftCertificateDtoRequest);

        return ApiResponse.of(
                "Gift Certificate with ID " + giftCertificate.getId() + " was created",
                GIFT_CERTIFICATE_API_PATH,
                HttpStatus.CREATED,
                giftCertificate
        );
    }

    /**
     * GET /api/v0/giftCertificates : Find Gift Certificates info
     *
     * @param pageable page number & page size values to return (not required)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<GiftCertificateDtoResponse>>> findAll(Pageable pageable) {
        PageResponse<GiftCertificateDtoResponse> giftCertificates = giftCertificateService.findAll(pageable);

        return ApiResponse.of(
                "All Gift Certificates: page_number: " + pageable.getPageNumber() +
                        "; page_size: " + pageable.getPageSize(),
                GIFT_CERTIFICATE_API_PATH,
                HttpStatus.OK,
                giftCertificates
        );
    }

    /**
     * GET /api/v0/giftCertificates : Find Gift Certificates info by criteria
     *
     * @param searchCriteria Gift Certificate searchCriteria to return (not required)
     * @param pageable page number & page size values to return (not required)
     */
    @GetMapping("/criteria")
    public ResponseEntity<ApiResponse<PageResponse<GiftCertificateDtoResponse>>> findAllByCriteria(
            @RequestBody(required = false) GiftCertificateCriteria searchCriteria,
            Pageable pageable) {
        PageResponse<GiftCertificateDtoResponse> giftCertificates = giftCertificateService
                .findAllByCriteria(searchCriteria, pageable);

        return ApiResponse.of(
                "Gift Certificates by criteria: tag_names: " + searchCriteria.getTagNames() +
                        "; description: " + searchCriteria.getDescription() +
                        "; sort_direction_name: " + searchCriteria.getSortDirectionName() +
                        "; sort_direction_date: " + searchCriteria.getSortDirectionDate() +
                        "; page_number: " + pageable.getPageNumber() +
                        "; page_size: " + pageable.getPageSize(),
                GIFT_CERTIFICATE_API_PATH + "/criteria",
                HttpStatus.OK,
                giftCertificates
        );
    }

    /**
     * GET /api/v0/giftCertificates/{id} : Find Gift Certificate info
     *
     * @param id Gift Certificate ID to return (required)
     * @throws EntityNotFoundException if the Gift Certificate with ID doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GiftCertificateDtoResponse>> findById(
            @PathVariable @NotNull @PositiveOrZero Long id) {
        GiftCertificateDtoResponse giftCertificate = giftCertificateService.findById(id);

        return ApiResponse.of(
                "Gift Certificate with ID " + giftCertificate.getId() + " was found",
                GIFT_CERTIFICATE_API_PATH + "/" + id,
                HttpStatus.OK,
                giftCertificate
        );
    }

    /**
     * PUT /api/v0/giftCertificates/{id} : Update an existing Gift Certificate info
     *
     * @param id Gift Certificate ID to return (required)
     * @param giftCertificateDtoRequest Gift Certificate object to update (required)
     * @throws EntityNotFoundException if the Gift Certificate with ID doesn't exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GiftCertificateDtoResponse>> update(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody @Valid GiftCertificateDtoRequest giftCertificateDtoRequest) {
        GiftCertificateDtoResponse giftCertificate = giftCertificateService
                .update(id, giftCertificateDtoRequest);

        return ApiResponse.of(
                "Changes were applied to the Gift Certificate with ID " + id,
                GIFT_CERTIFICATE_API_PATH + "/" + id,
                HttpStatus.OK,
                giftCertificate
        );
    }

    /**
     * PATCH /api/v0/giftCertificates/{id} : Partial Update an existing Gift Certificate info
     *
     * @param id Gift Certificate ID to return (required)
     * @param giftCertificateDtoRequest Gift Certificate object to update (required)
     * @throws EntityNotFoundException if the Gift Certificate with ID doesn't exist
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<GiftCertificateDtoResponse>> updatePartially(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody GiftCertificateDtoRequest giftCertificateDtoRequest) {
        GiftCertificateDtoResponse giftCertificate = giftCertificateService
                .updatePartially(id, giftCertificateDtoRequest);

        return ApiResponse.of(
                "Partial changes were applied to the Gift Certificate with ID " + id,
                GIFT_CERTIFICATE_API_PATH + "/" + id,
                HttpStatus.OK,
                giftCertificate
        );
    }

    /**
     * PATCH /api/v0/giftCertificates/add/{giftCertificateId}/{tagId} : Add Tag to Gift Certificate
     *
     * @param giftCertificateId Gift Certificate ID to return (required)
     * @param tagId Tag ID to return (required)
     * @throws EntityNotFoundException if the Gift Certificate ot Tag with ID doesn't exist
     */
    @PatchMapping("/add/{giftCertificateId}/{tagId}")
    public ResponseEntity<ApiResponse<GiftCertificateDtoResponse>> addTagToGiftCertificate(
            @PathVariable @NotNull @PositiveOrZero Long giftCertificateId,
            @PathVariable @NotNull @PositiveOrZero Long tagId) {
        GiftCertificateDtoResponse giftCertificate = giftCertificateService
                .addTagToGiftCertificate(giftCertificateId, tagId);

        return ApiResponse.of(
                "Gift Certificate by ID " + giftCertificateId + "with added Tag by ID " + tagId,
                GIFT_CERTIFICATE_API_PATH + "/" + giftCertificateId + "/" + tagId,
                HttpStatus.OK,
                giftCertificate
        );
    }

    /**
     * PATCH /api/v0/giftCertificates/delete/{giftCertificateId}/{tagId} : Delete Tag to Gift Certificate
     *
     * @param giftCertificateId Gift Certificate ID to return (required)
     * @param tagId Tag ID to return (required)
     * @throws EntityNotFoundException if the Gift Certificate or Tag with ID doesn't exist
     */
    @PatchMapping("/delete/{giftCertificateId}/{tagId}")
    public ResponseEntity<ApiResponse<GiftCertificateDtoResponse>> deleteTagFromGiftCertificate(
            @PathVariable @NotNull @PositiveOrZero Long giftCertificateId,
            @PathVariable @NotNull @PositiveOrZero Long tagId) {
        GiftCertificateDtoResponse giftCertificate = giftCertificateService
                .deleteTagFromGiftCertificate(giftCertificateId, tagId);

        return ApiResponse.of(
                "Gift Certificate by ID " + giftCertificateId + "with deleted Tag by ID " + tagId,
                GIFT_CERTIFICATE_API_PATH + "/" + giftCertificateId + "/" + tagId,
                HttpStatus.OK,
                giftCertificate
        );
    }

    /**
     * DELETE /api/v0/giftCertificates/{id} : Delete a Gift Certificate
     *
     * @param id Gift Certificate ID to return (required)
     * @throws EntityNotFoundException if the Gift Certificate with ID doesn't exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable @NotNull @PositiveOrZero Long id) {
        giftCertificateService.deleteById(id);

        return ApiResponse.of(
                "Gift Certificate with ID " + id + " was deleted",
                GIFT_CERTIFICATE_API_PATH + "/" + id,
                HttpStatus.NO_CONTENT,
                null
        );
    }
}

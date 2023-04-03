package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
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
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.ApiResponse;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.service.GiftCertificateService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.clevertec.ecl.controller.GiftCertificateController.GIFT_CERTIFICATE_API_PATH;
import static ru.clevertec.ecl.model.dto.response.ApiResponse.apiResponseEntity;

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

    public static final String GIFT_CERTIFICATE_API_PATH = "/api/v0/giftCertificates";

    /**
     * POST /api/v0/giftCertificates : Create a new Gift Certificate
     *
     * @param giftCertificateDtoRequest Gift Certificate object to create (required)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<GiftCertificateDtoResponse>> createGiftCertificate(
            @RequestBody @Valid GiftCertificateDtoRequest giftCertificateDtoRequest
    ) {
        GiftCertificateDtoResponse giftCertificate = giftCertificateService.createGiftCertificate(giftCertificateDtoRequest);

        return apiResponseEntity(
                "Gift Certificate with ID " + giftCertificate.getId() + " was created",
                GIFT_CERTIFICATE_API_PATH,
                HttpStatus.CREATED,
                ApiResponse.Color.SUCCESS,
                giftCertificate
        );
    }

    /**
     * GET /api/v0/giftCertificates : Find Gift Certificates info
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<GiftCertificateDtoResponse>>> findAllGiftCertificates() {
        List<GiftCertificateDtoResponse> giftCertificates = giftCertificateService.getAllGiftCertificates();

        return apiResponseEntity(
                "All Gift Certificates",
                GIFT_CERTIFICATE_API_PATH,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                giftCertificates
        );
    }

    /**
     * GET /api/v0/giftCertificates : Find Gift Certificates info by criteria
     *
     * @param searchCriteria Gift Certificate searchCriteria to return (not required)
     */
    @GetMapping("/criteria")
    public ResponseEntity<ApiResponse<List<GiftCertificateDtoResponse>>> findAllGiftCertificatesByCriteria(
            @RequestBody(required = false) GiftCertificateCriteria searchCriteria
    ) {
        if (searchCriteria == null) {
            searchCriteria = GiftCertificateCriteria.builder().build();
        }

        List<GiftCertificateDtoResponse> giftCertificates = giftCertificateService.getAllGiftCertificatesByCriteria(searchCriteria);

        return apiResponseEntity(
                "Gift Certificates by criteria: tag_name: " + searchCriteria.getTagName() +
                        "; description: " + searchCriteria.getDescription() +
                        "; sort_type_name: " + searchCriteria.getSortTypeName() +
                        "; sort_type_date: " + searchCriteria.getSortTypeDate(),
                GIFT_CERTIFICATE_API_PATH,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                giftCertificates
        );
    }

    /**
     * GET /api/v0/giftCertificates/{id} : Find Gift Certificate info
     *
     * @param id Gift Certificate id to return (required)
     * @throws GiftCertificateNotFoundException if the Gift Certificate with id doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GiftCertificateDtoResponse>> findGiftCertificateById(@PathVariable @Valid @NotNull Long id) {
        GiftCertificateDtoResponse giftCertificate = giftCertificateService.getGiftCertificateById(id);

        return apiResponseEntity(
                "Gift Certificate with ID " + giftCertificate.getId() + " was found",
                GIFT_CERTIFICATE_API_PATH + "/" + id,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                giftCertificate
        );
    }

    /**
     * PUT /api/v0/giftCertificates/{id} : Update an existing Gift Certificate info
     *
     * @param id Gift Certificate id to return (required)
     * @param giftCertificateDtoRequest Gift Certificate object to update (required)
     * @throws GiftCertificateNotFoundException if the Gift Certificate with id doesn't exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GiftCertificateDtoResponse>> updateGiftCertificateById(
            @PathVariable @Valid @NotNull Long id,
            @RequestBody @Valid GiftCertificateDtoRequest giftCertificateDtoRequest
    ) {
        GiftCertificateDtoResponse giftCertificate = giftCertificateService.updateGiftCertificateById(id, giftCertificateDtoRequest);

        return apiResponseEntity(
                "Changes were applied to the Gift Certificate with ID " + id,
                GIFT_CERTIFICATE_API_PATH + "/" + id,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                giftCertificate
        );
    }

    /**
     * PATCH /api/v0/giftCertificates/{id} : Partial Update an existing Gift Certificate info
     *
     * @param id Gift Certificate id to return (required)
     * @param giftCertificateDtoRequest Gift Certificate object to update (required)
     * @throws GiftCertificateNotFoundException if the Gift Certificate with id doesn't exist
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<GiftCertificateDtoResponse>> updateGiftCertificateByIdPartially(
            @PathVariable @Valid @NotNull Long id,
            @RequestBody GiftCertificateDtoRequest giftCertificateDtoRequest
    ) {
        GiftCertificateDtoResponse giftCertificate = giftCertificateService.updateGiftCertificateByIdPartially(id, giftCertificateDtoRequest);

        return apiResponseEntity(
                "Partial changes were applied to the Gift Certificate with ID " + id,
                GIFT_CERTIFICATE_API_PATH + "/" + id,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                giftCertificate
        );
    }

    /**
     * DELETE /api/v0/giftCertificates/{id} : Delete a Gift Certificate
     *
     * @param id Gift Certificate id to return (required)
     * @throws GiftCertificateNotFoundException if the Gift Certificate with id doesn't exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGiftCertificateById(@PathVariable @Valid @NotNull Long id) {
        giftCertificateService.deleteGiftCertificateById(id);

        return apiResponseEntity(
                "Gift Certificate with ID " + id + " was deleted",
                GIFT_CERTIFICATE_API_PATH + "/" + id,
                HttpStatus.NO_CONTENT,
                ApiResponse.Color.SUCCESS,
                null
        );
    }
}

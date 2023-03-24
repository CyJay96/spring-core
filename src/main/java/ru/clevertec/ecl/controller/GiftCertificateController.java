package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.ApiResponse;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.util.List;

import static ru.clevertec.ecl.controller.GiftCertificateController.GIFT_CERTIFICATE_API_PATH;
import static ru.clevertec.ecl.model.dto.response.ApiResponse.apiResponseEntity;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = GIFT_CERTIFICATE_API_PATH)
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    public static final String GIFT_CERTIFICATE_API_PATH = "/api/v0/giftCertificates";

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

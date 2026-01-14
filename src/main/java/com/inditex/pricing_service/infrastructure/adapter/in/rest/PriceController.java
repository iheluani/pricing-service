package com.inditex.pricing_service.infrastructure.adapter.in.rest;

import com.inditex.pricing_service.application.port.in.GetApplicablePriceQuery;
import com.inditex.pricing_service.domain.model.Price;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/prices", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class PriceController {

    private final GetApplicablePriceQuery getApplicablePriceQuery;

    public PriceController(GetApplicablePriceQuery getApplicablePriceQuery) {
        this.getApplicablePriceQuery = getApplicablePriceQuery;
    }

    @GetMapping
    public ResponseEntity<PriceResponseDto> getApplicablePrice(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam @Min(1) long productId,
            @RequestParam @Min(1) long brandId
    ) {
        Price price = getApplicablePriceQuery.getApplicablePrice(applicationDate, productId, brandId);

        PriceResponseDto body = new PriceResponseDto(
                price.getProductId(),
                price.getBrandId(),
                price.getPriceList(),
                price.getStartDate().format(DateTimeFormatter.ISO_DATE_TIME),
                price.getEndDate().format(DateTimeFormatter.ISO_DATE_TIME),
                price.getAmount(),
                price.getCurrency()
        );

        String etagSource =
                body.productId() + "|" +
                        body.brandId() + "|" +
                        body.priceList() + "|" +
                        body.startDate() + "|" +
                        body.endDate() + "|" +
                        body.price() + "|" +
                        body.currency();

        String etag = Integer.toHexString(etagSource.hashCode());

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .eTag("\"" + etag + "\"")
                .body(body);
    }
}
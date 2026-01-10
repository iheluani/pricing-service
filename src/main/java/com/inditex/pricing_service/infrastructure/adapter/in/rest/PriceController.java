package com.inditex.pricing_service.infrastructure.adapter.in.rest;

import com.inditex.pricing_service.application.port.in.GetApplicablePriceQuery;
import com.inditex.pricing_service.domain.model.Price;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class PriceController {

    private final GetApplicablePriceQuery getApplicablePriceQuery;

    public PriceController(GetApplicablePriceQuery getApplicablePriceQuery) {
        this.getApplicablePriceQuery = getApplicablePriceQuery;
    }

    @GetMapping("/prices/applicable")
    public PriceResponseDto getApplicablePrice(
            @RequestParam("applicationDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam("productId") long productId,
            @RequestParam("brandId") long brandId
    ) {
        Price price = getApplicablePriceQuery.getApplicablePrice(applicationDate, productId, brandId);

        return new PriceResponseDto(
                price.getProductId(),
                price.getBrandId(),
                price.getPriceList(),
                price.getStartDate().toString(),
                price.getEndDate().toString(),
                price.getAmount(),
                price.getCurrency()
        );
    }
}
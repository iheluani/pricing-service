package com.inditex.pricing_service.application.service;

import com.inditex.pricing_service.application.port.in.GetApplicablePriceQuery;
import com.inditex.pricing_service.application.port.out.PriceRepositoryPort;
import com.inditex.pricing_service.domain.exception.PriceNotFoundException;
import com.inditex.pricing_service.domain.model.Price;

import java.time.LocalDateTime;

public class GetApplicablePriceService implements GetApplicablePriceQuery {

    private final PriceRepositoryPort priceRepositoryPort;

    public GetApplicablePriceService(PriceRepositoryPort priceRepositoryPort) {
        this.priceRepositoryPort = priceRepositoryPort;
    }

    @Override
    public Price getApplicablePrice(LocalDateTime applicationDate, long productId, long brandId) {
        return priceRepositoryPort.findApplicablePrice(applicationDate, productId, brandId)
                .orElseThrow(() -> new PriceNotFoundException(brandId, productId, applicationDate.toString()));
    }
}
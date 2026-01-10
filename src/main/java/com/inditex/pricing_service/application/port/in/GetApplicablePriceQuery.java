package com.inditex.pricing_service.application.port.in;

import com.inditex.pricing_service.domain.model.Price;

import java.time.LocalDateTime;

public interface GetApplicablePriceQuery {

    Price getApplicablePrice(LocalDateTime applicationDate, long productId, long brandId);
}
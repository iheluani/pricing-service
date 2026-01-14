package com.inditex.pricing_service.infrastructure.adapter.in.rest;

import java.math.BigDecimal;

public record PriceResponseDto(
        long productId,
        long brandId,
        long priceList,
        String startDate,
        String endDate,
        BigDecimal price,
        String currency
) { }
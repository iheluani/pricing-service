package com.inditex.pricing_service.infrastructure.adapter.in.rest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponseDto(
        long productId,
        long brandId,
        long priceList,
        String startDate,
        String endDate,
        BigDecimal price,
        String currency
) { }
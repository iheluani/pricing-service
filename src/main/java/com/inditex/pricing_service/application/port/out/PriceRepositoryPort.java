package com.inditex.pricing_service.application.port.out;

import com.inditex.pricing_service.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepositoryPort {

    Optional<Price> findApplicablePrice(LocalDateTime applicationDate, long productId, long brandId);
}
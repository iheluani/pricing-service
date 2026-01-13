package com.inditex.pricing_service.infrastructure.config;

import com.inditex.pricing_service.application.port.in.GetApplicablePriceQuery;
import com.inditex.pricing_service.domain.model.Price;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;

public class CachedGetApplicablePriceQuery implements GetApplicablePriceQuery {

    private final GetApplicablePriceQuery delegate;

    public CachedGetApplicablePriceQuery(GetApplicablePriceQuery delegate) {
        this.delegate = delegate;
    }

    @Override
    @Cacheable(
            cacheNames = "applicablePrice",
            key = "#brandId + '-' + #productId + '-' + #applicationDate.toString()",
            unless = "#result == null"
    )
    public Price getApplicablePrice(LocalDateTime applicationDate, long productId, long brandId) {
        return delegate.getApplicablePrice(applicationDate, productId, brandId);
    }
}
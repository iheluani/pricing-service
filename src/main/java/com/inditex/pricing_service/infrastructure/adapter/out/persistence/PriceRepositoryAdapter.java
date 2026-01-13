package com.inditex.pricing_service.infrastructure.adapter.out.persistence;

import com.inditex.pricing_service.application.port.out.PriceRepositoryPort;
import com.inditex.pricing_service.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final SpringDataPriceRepository springDataPriceRepository;

    public PriceRepositoryAdapter(SpringDataPriceRepository springDataPriceRepository) {
        this.springDataPriceRepository = springDataPriceRepository;
    }

    @Override
    public Optional<Price> findApplicablePrice(LocalDateTime applicationDate, long productId, long brandId) {
        return springDataPriceRepository
                .findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDescStartDateDesc(
                        brandId,
                        productId,
                        applicationDate,
                        applicationDate
                )
                .map(this::toDomain);
    }

    private Price toDomain(PriceJpaEntity entity) {
        return new Price(
                entity.getProductId(),
                entity.getBrandId(),
                entity.getPriceList(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPrice(),
                entity.getCurrency()
        );
    }
}
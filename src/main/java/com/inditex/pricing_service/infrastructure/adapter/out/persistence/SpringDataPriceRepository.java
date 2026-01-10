package com.inditex.pricing_service.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPriceRepository extends JpaRepository<PriceJpaEntity, Long> {

}

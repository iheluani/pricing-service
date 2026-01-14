package com.inditex.pricing_service.infrastructure.config;

import com.inditex.pricing_service.application.port.in.GetApplicablePriceQuery;
import com.inditex.pricing_service.application.port.out.PriceRepositoryPort;
import com.inditex.pricing_service.application.service.GetApplicablePriceService;
import com.inditex.pricing_service.infrastructure.adapter.out.persistence.PriceRepositoryAdapter;
import com.inditex.pricing_service.infrastructure.adapter.out.persistence.SpringDataPriceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public PriceRepositoryPort priceRepositoryPort(SpringDataPriceRepository springDataPriceRepository) {
        return new PriceRepositoryAdapter(springDataPriceRepository);
    }

    @Bean
    public GetApplicablePriceQuery getApplicablePriceQuery(PriceRepositoryPort priceRepositoryPort) {
        GetApplicablePriceQuery base = new GetApplicablePriceService(priceRepositoryPort);
        return new CachedGetApplicablePriceQuery(base);
    }
}
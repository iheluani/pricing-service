package com.inditex.pricing_service.infrastructure.config;

import com.inditex.pricing_service.application.port.in.GetApplicablePriceQuery;
import com.inditex.pricing_service.application.port.out.PriceRepositoryPort;
import com.inditex.pricing_service.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class CachingIT {

    @Autowired
    private GetApplicablePriceQuery query;

    @MockitoBean
    private PriceRepositoryPort priceRepositoryPort;

    @Test
    void shouldCacheResult_whenSameRequestIsRepeated() {
        LocalDateTime date = LocalDateTime.parse("2020-06-14T16:00:00");
        long productId = 35455L;
        long brandId = 1L;

        Price price = new Price(
                productId,
                brandId,
                2L,
                LocalDateTime.parse("2020-06-14T15:00:00"),
                LocalDateTime.parse("2020-06-14T18:30:00"),
                new BigDecimal("25.45"),
                "EUR"
        );

        when(priceRepositoryPort.findApplicablePrice(date, productId, brandId))
                .thenReturn(Optional.of(price));

        query.getApplicablePrice(date, productId, brandId);
        query.getApplicablePrice(date, productId, brandId);

        verify(priceRepositoryPort, times(1)).findApplicablePrice(date, productId, brandId);
    }
}

package com.inditex.pricing_service.infrastructure.adapter.out.persistence;

import com.inditex.pricing_service.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class PriceRepositoryAdapterTest {

    @Test
    void mapsJpaEntityToDomainCorrectly() {
        SpringDataPriceRepository springRepo = mock(SpringDataPriceRepository.class);
        PriceRepositoryAdapter adapter = new PriceRepositoryAdapter(springRepo);

        LocalDateTime appDate = LocalDateTime.parse("2020-06-14T10:00:00");

        PriceJpaEntity entity = new PriceJpaEntity();
        ReflectionTestUtils.setField(entity, "brandId", 1L);
        ReflectionTestUtils.setField(entity, "productId", 35455L);
        ReflectionTestUtils.setField(entity, "priceList", 1L);
        ReflectionTestUtils.setField(entity, "priority", 0);
        ReflectionTestUtils.setField(entity, "startDate", LocalDateTime.parse("2020-06-14T00:00:00"));
        ReflectionTestUtils.setField(entity, "endDate", LocalDateTime.parse("2020-12-31T23:59:59"));
        ReflectionTestUtils.setField(entity, "price", new BigDecimal("35.50"));
        ReflectionTestUtils.setField(entity, "currency", "EUR");

        when(springRepo.findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDescStartDateDesc(
                1L, 35455L, appDate, appDate
        )).thenReturn(Optional.of(entity));

        Optional<Price> result = adapter.findApplicablePrice(appDate, 35455L, 1L);

        assertThat(result).isPresent();
        assertThat(result.get().getBrandId()).isEqualTo(1L);
        assertThat(result.get().getProductId()).isEqualTo(35455L);
        assertThat(result.get().getPriceList()).isEqualTo(1L);
        assertThat(result.get().getAmount()).isEqualByComparingTo("35.50");
        assertThat(result.get().getCurrency()).isEqualTo("EUR");

        verify(springRepo).findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDescStartDateDesc(
                1L, 35455L, appDate, appDate
        );
        verifyNoMoreInteractions(springRepo);
    }
}
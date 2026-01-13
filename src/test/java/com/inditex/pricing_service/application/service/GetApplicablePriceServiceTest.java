package com.inditex.pricing_service.application.service;

import com.inditex.pricing_service.application.port.out.PriceRepositoryPort;
import com.inditex.pricing_service.domain.exception.PriceNotFoundException;
import com.inditex.pricing_service.domain.model.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class GetApplicablePriceServiceTest {

    @Test
    void returnsPriceWhenRepositoryFindsOne() {
        PriceRepositoryPort repo = mock(PriceRepositoryPort.class);
        GetApplicablePriceService service = new GetApplicablePriceService(repo);

        LocalDateTime appDate = LocalDateTime.parse("2020-06-14T10:00:00");
        Price expected = new Price(
                35455L,
                1L,
                1L,
                LocalDateTime.parse("2020-06-14T00:00:00"),
                LocalDateTime.parse("2020-12-31T23:59:59"),
                new BigDecimal("35.50"),
                "EUR"
        );

        when(repo.findApplicablePrice(appDate, 35455L, 1L)).thenReturn(Optional.of(expected));

        Price result = service.getApplicablePrice(appDate, 35455L, 1L);

        assertThat(result).isSameAs(expected);
        verify(repo).findApplicablePrice(appDate, 35455L, 1L);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void throwsPriceNotFoundWhenRepositoryReturnsEmpty() {
        PriceRepositoryPort repo = mock(PriceRepositoryPort.class);
        GetApplicablePriceService service = new GetApplicablePriceService(repo);

        LocalDateTime appDate = LocalDateTime.parse("2020-06-14T10:00:00");
        when(repo.findApplicablePrice(appDate, 35455L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getApplicablePrice(appDate, 35455L, 1L))
                .isInstanceOf(PriceNotFoundException.class);

        verify(repo).findApplicablePrice(appDate, 35455L, 1L);
        verifyNoMoreInteractions(repo);
    }
}
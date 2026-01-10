package com.inditex.pricing_service.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public final class Price {

    private final long productId;
    private final long brandId;
    private final long priceList;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final BigDecimal amount;
    private final String currency;

    public Price(long productId,
                 long brandId,
                 long priceList,
                 LocalDateTime startDate,
                 LocalDateTime endDate,
                 BigDecimal amount,
                 String currency) {

        this.productId = productId;
        this.brandId = brandId;
        this.priceList = priceList;
        this.startDate = Objects.requireNonNull(startDate, "startDate must not be null");
        this.endDate = Objects.requireNonNull(endDate, "endDate must not be null");
        this.amount = Objects.requireNonNull(amount, "amount must not be null");
        this.currency = Objects.requireNonNull(currency, "currency must not be null");
    }

    public long getProductId() { return productId; }
    public long getBrandId() { return brandId; }
    public long getPriceList() { return priceList; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
}

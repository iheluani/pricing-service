package com.inditex.pricing_service.domain.exception;

public class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(long brandId, long productId, String applicationDate) {
        super("No applicable price found for brandId=" + brandId +
                ", productId=" + productId +
                ", applicationDate=" + applicationDate);
    }
}

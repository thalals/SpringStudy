package com.entity_isolation.isolation.orders.model.dto;

import java.math.BigDecimal;

public record OrderPaymentInfoDto(
    String paymentType,
    BigDecimal totalAmount,
    BigDecimal receivedAmount,
    BigDecimal deliveryFee
) {

    public static OrderPaymentInfoDto of(String paymentType, BigDecimal receivedAmount,
        BigDecimal deliveryFee) {

        BigDecimal totalAmount = receivedAmount.add(deliveryFee);
        return new OrderPaymentInfoDto(paymentType, totalAmount, receivedAmount, deliveryFee);
    }

}

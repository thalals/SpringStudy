package com.entity_isolation.isolation.orders.model.mapper;

import java.math.BigDecimal;

public record OrderPaymentInfoMapper(
    String paymentType,
    BigDecimal totalAmount,
    BigDecimal receivedAmount,
    BigDecimal deliveryFee
) {

    public static OrderPaymentInfoMapper of(String paymentType, BigDecimal receivedAmount,
        BigDecimal deliveryFee) {

        BigDecimal totalAmount = receivedAmount.add(deliveryFee);
        return new OrderPaymentInfoMapper(paymentType, totalAmount, receivedAmount, deliveryFee);
    }

}

package com.entity_isolation.isolation.orders.dto.request;

import com.entity_isolation.isolation.orders.model.Order;
import java.math.BigDecimal;

public record OrderCreateRequest(
    String paymentType,
    BigDecimal receivedAmount,
    BigDecimal deliveryFee,
    String customerName
) {

    public Order toEntity() {
        return Order.createOf(
            paymentType,
            receivedAmount,
            deliveryFee,
            customerName
        );
    }
}

package com.entity_isolation.isolation.orders.dto;

import com.entity_isolation.isolation.orders.model.Order;
import java.math.BigDecimal;

public record OrderCreateRequestNew(
    String paymentType,
    BigDecimal receivedAmount,
    BigDecimal deliveryFee,
    //국가에 따라 customer 이름 수집 변경
    String country,
    String firstName,
    String middleName,
    String lastName,
    String kanaFirstName,
    String kanaLastName
) {

    public Order toEntity() {
        return Order.createOf(
            paymentType,
            receivedAmount,
            deliveryFee,
            getCustomerName()
        );
    }

    private String getCustomerName() {

        if ("일본".equals(country)) {
            return kanaFirstName + " " + kanaLastName;
        }

        return firstName + " " + middleName + " " + lastName;

    }

}

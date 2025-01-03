package com.entity_isolation.isolation.orders.dto.request;

import com.entity_isolation.isolation.orders.model.Order;
import com.entity_isolation.isolation.orders.model.dto.OrderCustomerDto;
import com.entity_isolation.isolation.orders.model.dto.OrderPaymentInfoDto;
import java.math.BigDecimal;

public record OrderCreateRequestNew(
    String paymentType,
    BigDecimal receivedAmount,
    BigDecimal deliveryFee,
    //국가에 따라 customer 이름 수집 변경 및 email, phone 추가 수집
    String country,
    String firstName,
    String middleName,
    String lastName,
    String kanaFirstName,
    String kanaLastName,
    String email,
    String phone
) {

    public Order toEntity() {
        return Order.createOf(
            paymentType,
            receivedAmount,
            deliveryFee,
            getCustomerName()
        );
    }

    public String getCustomerName() {

        if ("일본".equals(country)) {
            return kanaFirstName + " " + kanaLastName;
        }

        return firstName + " " + middleName + " " + lastName;

    }

    //mapper 이용
    public Order toEntityFromMapper() {

        OrderCustomerDto customerMapper = new OrderCustomerDto(getCustomerName(), email, phone);
        OrderPaymentInfoDto paymentInfoMapper = OrderPaymentInfoDto.of(paymentType,
            receivedAmount, deliveryFee);

        return Order.createOf(customerMapper, paymentInfoMapper);
    }

}

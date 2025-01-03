package com.entity_isolation.isolation.orders.mapper;

import com.entity_isolation.isolation.orders.application.request.OrderCreateRequest;
import com.entity_isolation.isolation.orders.application.request.OrderCreateRequestNew;
import com.entity_isolation.isolation.orders.application.response.OrderCreateResponse;
import com.entity_isolation.isolation.orders.model.Order;
import com.entity_isolation.isolation.orders.model.dto.OrderCustomerDto;
import com.entity_isolation.isolation.orders.model.dto.OrderPaymentInfoDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderMapper {

    public static Order toEntity(final OrderCreateRequest request) {
        return Order.createOf(
            request.paymentType(),
            request.receivedAmount(),
            request.deliveryFee(),
            request.customerName()
        );
    }

    public static Order toEntity(final OrderCreateRequestNew request) {

        final OrderCustomerDto customerMapper = new OrderCustomerDto(
            request.getCustomerName(),
            request.email(),
            request.phone()
        );

        final OrderPaymentInfoDto paymentInfoMapper = OrderPaymentInfoDto.of(
            request.paymentType(),
            request.receivedAmount(),
            request.deliveryFee()
        );

        return Order.createOf(customerMapper, paymentInfoMapper);
    }

    public static OrderCreateResponse toResponse(final Order order) {
        return new OrderCreateResponse();
    }
}

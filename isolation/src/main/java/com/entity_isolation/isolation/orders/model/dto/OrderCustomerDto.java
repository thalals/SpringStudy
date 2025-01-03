package com.entity_isolation.isolation.orders.model.dto;

public record OrderCustomerDto(
    String name,
    String email,
    String phone
) {

}

package com.entity_isolation.isolation.orders.model;

import com.entity_isolation.isolation.orders.dto.OrderCreateRequest;
import com.entity_isolation.isolation.orders.dto.OrderCreateRequestNew;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String orderNumber;
    private String paymentType;
    private BigDecimal totalAmount;
    private BigDecimal receivedAmount;
    private BigDecimal returnAmount;
    private BigDecimal deliveryFee;
    private String refundBank;
    private String customerName;
    private String orderStatus;


    @Builder(access = AccessLevel.PRIVATE)
    private Order(String orderNumber, String paymentType, BigDecimal totalAmount,
        BigDecimal receivedAmount, BigDecimal returnAmount, BigDecimal deliveryFee,
        String customerName,
        String orderStatus) {

        this.orderNumber = orderNumber;
        this.paymentType = paymentType;
        this.totalAmount = totalAmount;
        this.receivedAmount = receivedAmount;
        this.returnAmount = returnAmount;
        this.deliveryFee = deliveryFee;
        this.customerName = customerName;
        this.orderStatus = orderStatus;
    }

    public static Order createOfRequest(OrderCreateRequest request) {
        return Order.builder()
            .orderNumber(generateOrderNumber())
            .paymentType(request.paymentType())
            .totalAmount(request.receivedAmount().add(request.deliveryFee()))
            .receivedAmount(request.receivedAmount())
            .deliveryFee(request.deliveryFee())
            .customerName(request.customerName())
            .build();
    }

//    public static Order createOfRequest(OrderCreateRequestNew request) {
//        return Order.builder()
//            .orderNumber(generateOrderNumber())
//            .paymentType(request.paymentType())
//            .totalAmount(request.receivedAmount().add(request.deliveryFee()))
//            .receivedAmount(request.receivedAmount())
//            .deliveryFee(request.deliveryFee())
//            .customerName(request.getCustomerName())
//            .build();
//    }

    public static Order createOf(String paymentType, BigDecimal receivedAmount,
        BigDecimal deliveryFee, String customerName) {

        return Order.builder()
            .orderNumber(generateOrderNumber())
            .paymentType(paymentType)
            .totalAmount(receivedAmount.add(deliveryFee))
            .receivedAmount(receivedAmount)
            .deliveryFee(deliveryFee)
            .customerName(customerName)
            .build();
    }

    private static String generateOrderNumber() {
        return UUID.randomUUID().toString();
    }

}

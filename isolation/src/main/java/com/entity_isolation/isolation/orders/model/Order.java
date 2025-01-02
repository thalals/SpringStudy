package com.entity_isolation.isolation.orders.model;

import com.entity_isolation.isolation.orders.dto.OrderCreateRequest;
import com.entity_isolation.isolation.orders.model.mapper.OrderCustomerMapper;
import com.entity_isolation.isolation.orders.model.mapper.OrderPaymentInfoMapper;
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
    private String customerEmail;
    private String customerPhone;
    private String orderStatus;


    @Builder(access = AccessLevel.PRIVATE)
    private Order(String orderNumber, String paymentType, BigDecimal totalAmount,
        BigDecimal receivedAmount, BigDecimal returnAmount, BigDecimal deliveryFee,
        String customerName,String customerEmail, String customerPhone, String orderStatus) {

        this.orderNumber = orderNumber;
        this.paymentType = paymentType;
        this.totalAmount = totalAmount;
        this.receivedAmount = receivedAmount;
        this.returnAmount = returnAmount;
        this.deliveryFee = deliveryFee;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.customerName = customerName;
        this.orderStatus = orderStatus;
    }

    //Request 레이어가 Entity(Domain) 레이어를 침범 -> Request 가 추가되거나 변경되면 Entity 까지 변경의 범위가 확장 됨
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

    // Request Dto 를 받지않고, 데이터만 받음
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

    //Mapper
    public static Order createOf(OrderCustomerMapper customer, OrderPaymentInfoMapper paymentInfo) {

        return Order.builder()
            .orderNumber(generateOrderNumber())
            .paymentType(paymentInfo.paymentType())
            .totalAmount(paymentInfo.totalAmount())
            .receivedAmount(paymentInfo.receivedAmount())
            .deliveryFee(paymentInfo.deliveryFee())
            .customerName(customer.name())
            .customerEmail(customer.email())
            .customerPhone(customer.phone())
            .build();
    }

    private static String generateOrderNumber() {
        return UUID.randomUUID().toString();
    }

}

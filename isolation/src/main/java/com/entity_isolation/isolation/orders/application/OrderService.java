package com.entity_isolation.isolation.orders.application;

import com.entity_isolation.isolation.orders.dao.OrderRepository;
import com.entity_isolation.isolation.orders.application.request.OrderCreateRequest;
import com.entity_isolation.isolation.orders.application.response.OrderCreateResponse;
import com.entity_isolation.isolation.orders.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderCreateResponse create(final OrderCreateRequest request) {

        final Order orderToRequest = Order.createOfRequest(request);

        final Order order = Order.createOf(
                request.paymentType(),
                request.receivedAmount(),
                request.deliveryFee(),
                request.customerName()
            );

        final Order orderToEntity = request.toEntity();

        orderRepository.save(order);
        orderRepository.save(orderToRequest);
        orderRepository.save(orderToEntity);
        return null;
    }
}

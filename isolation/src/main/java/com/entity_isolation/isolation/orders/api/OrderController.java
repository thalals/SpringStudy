package com.entity_isolation.isolation.orders.api;

import com.entity_isolation.isolation.orders.application.OrderService;
import com.entity_isolation.isolation.orders.application.request.OrderCreateRequest;
import com.entity_isolation.isolation.orders.application.response.OrderCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderCreateResponse> create(
        @RequestBody OrderCreateRequest orderCreateRequest) {

        final OrderCreateResponse response = orderService.create(orderCreateRequest);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }
}

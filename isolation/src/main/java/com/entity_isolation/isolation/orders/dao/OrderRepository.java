package com.entity_isolation.isolation.orders.dao;

import com.entity_isolation.isolation.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}

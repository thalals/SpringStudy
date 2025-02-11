package com.entity_isolation.isolation.orders.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExceptionService {

    @Transactional
    public void exception() {

        throw new RuntimeException("");
    }
}

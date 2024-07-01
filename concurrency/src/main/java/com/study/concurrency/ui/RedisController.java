package com.study.concurrency.ui;

import com.study.concurrency.application.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    @GetMapping("/get")
    public ResponseEntity<String> get() {

        redisService.getTickets();

        return ResponseEntity.ok("성공");
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id) {

        redisService.updateTicketQuantity(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/create")
    public ResponseEntity<Object> create() {
        redisService.create();
        return ResponseEntity.ok().build();
    }
}

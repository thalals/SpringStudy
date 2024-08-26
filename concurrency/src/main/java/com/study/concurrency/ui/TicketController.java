package com.study.concurrency.ui;

import com.study.concurrency.application.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/kafka")
    ResponseEntity<Object> go() {

        ticketService.reservationToRedisAndKafka(1L);
        return ResponseEntity.ok().build();
    }
}

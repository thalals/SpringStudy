package org.example.redis.ui;


import lombok.RequiredArgsConstructor;
import org.example.redis.application.TicketService;
import org.example.redis.domain.Ticket;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RedisController {

    private final TicketService ticketService;

    @GetMapping("/get")
    public ResponseEntity<List<Ticket>> getRedis() {

        List<Ticket> tickets = ticketService.getTickets();
        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id) {

        ticketService.updateTicketQuantity(id);
        return ResponseEntity.ok().build();
    }
}

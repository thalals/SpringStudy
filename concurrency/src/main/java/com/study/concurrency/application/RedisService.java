package com.study.concurrency.application;

import com.study.concurrency.domain.Repository.TicketRepository;
import com.study.concurrency.domain.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final TicketRepository ticketRepository;

    public void getTickets() {
        ticketRepository.findAll().forEach(System.out::println);
    }

    public void updateTicketQuantity(long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId).get();
        ticket.changeQuantity(-1);

        ticketRepository.save(ticket);
    }

    public void create() {

        for (int i = 1; i <= 10000; i++) {
            ticketRepository.save(Ticket.create(Long.valueOf(i), i));
            ticketRepository.save(Ticket.create(Long.valueOf(i+1), i+1));
            ticketRepository.save(Ticket.create(Long.valueOf(i+2), i+2));
            ticketRepository.save(Ticket.create(Long.valueOf(i+3), i+3));
            ticketRepository.save(Ticket.create(Long.valueOf(i+4), i+4));
        }
    }
}

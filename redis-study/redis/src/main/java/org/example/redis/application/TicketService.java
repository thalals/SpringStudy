package org.example.redis.application;


import lombok.RequiredArgsConstructor;
import org.example.redis.domain.Repository.TicketRepository;
import org.example.redis.domain.Ticket;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    @Cacheable(cacheNames = "findTicketAll", key = "#root.methodName", cacheManager = "cacheManager")
    public List<Ticket> getTickets() {
        return ticketRepository.findAll();
    }

    @CacheEvict(cacheNames = "findTicketAll", allEntries = true, cacheManager = "cacheManager")
    public void updateTicketQuantity(long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId).get();
        ticket.changeQuantity(-1);

        ticketRepository.save(ticket);
    }
}

package com.study.concurrency.application;

import com.study.concurrency.domain.Ticket;
import com.study.concurrency.domain.TicketRepository;
import com.study.concurrency.domain.TicketReservation;
import com.study.concurrency.domain.TicketReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketReservationRepository ticketReservationRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public void reservation(final Long ticketId) {

        final Ticket ticket = ticketRepository.findById(ticketId).get();
        ticket.decrease();

        ticketReservationRepository.save(TicketReservation.create(ticket, ticket.getNumber()));
    }
}

package com.study.concurrency.infra;

import com.study.concurrency.domain.Repository.TicketRepository;
import com.study.concurrency.domain.Repository.TicketReservationRepository;
import com.study.concurrency.domain.Ticket;
import com.study.concurrency.domain.TicketReservation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketReservationConsumer {

    private static final Logger log = LoggerFactory.getLogger(TicketReservationConsumer.class);
    private final TicketRepository ticketRepository;
    private final TicketReservationRepository ticketReservationRepository;

    @KafkaListener(topics = "ticket-reservation", groupId = "group_1")
    public void listener(Long ticketId) {

        log.info("[CONSUMER] received ticket id {}", ticketId);

        final Ticket ticket = ticketRepository.findById(ticketId).get();

        ticket.decrease();
        ticketRepository.save(ticket);
        ticketReservationRepository.save(TicketReservation.create(ticket, ticket.getNumber()));
    }
}

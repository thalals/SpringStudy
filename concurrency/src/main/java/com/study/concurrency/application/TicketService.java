package com.study.concurrency.application;

import com.study.concurrency.domain.Ticket;
import com.study.concurrency.domain.TicketRepository;
import com.study.concurrency.domain.TicketReservation;
import com.study.concurrency.domain.TicketReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketService {

    private final TicketReservationRepository ticketReservationRepository;
    private final TicketRepository ticketRepository;
    private final DataSource dataSource;

    @Synchronized
    public void reservationWithoutTransactional(final Long ticketId) throws SQLException {

        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        try {
            final Ticket ticket = ticketRepository.findById(ticketId).get();
            ticket.decrease();

            ticketRepository.save(ticket);
            ticketReservationRepository.save(TicketReservation.create(ticket, ticket.getNumber()));

            connection.commit();
            connection.close();
        } catch (Exception e) {
            connection.rollback();
            log.info("실패");
        }
    }
}

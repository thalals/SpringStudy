package com.study.concurrency.application;

import com.study.concurrency.domain.Repository.TicketCountRepository;
import com.study.concurrency.domain.Repository.TicketRepository;
import com.study.concurrency.domain.Repository.TicketReservationRepository;
import com.study.concurrency.domain.Ticket;
import com.study.concurrency.domain.TicketReservation;
import com.study.concurrency.infra.TicketReservationConsumer;
import com.study.concurrency.infra.TicketReservationProducer;
import jakarta.transaction.Transactional;
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

    private final TicketCountRepository ticketCountRepository;
    private final TicketReservationProducer ticketReservationProducer;

    @Synchronized
    public void reservationWithoutTransactional(final Long ticketId) throws SQLException {

        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        try {
            final Ticket ticket = ticketRepository.findById(ticketId).get();

            reservationSuccess(ticket);

            connection.commit();
            connection.close();
        } catch (Exception e) {
            connection.rollback();
            log.info("실패");
        }
    }

    private void reservationSuccess(final Ticket ticket) {
        ticket.decrease();
        ticketRepository.save(ticket);
        ticketReservationRepository.save(TicketReservation.create(ticket, ticket.getNumber()));
    }

    @Transactional
    public void reservationToRedisAndKafka(final Long ticketId) {

        //redis 로 동시성 제어
        final String key = "ticket" + ticketId;

        Long increment = ticketCountRepository.increment(key);
        log.info("increment : " + increment);

        if (increment <= 100) {
            // 100명 컷은 되는데, 레디스가 처리가 빨라서 db 쪽 데이터 처리량이 못따라감, 데드락 걸림
            // 이래서 카프카 써야하나봄

            ticketReservationProducer.reserve(ticketId, increment);
        }
    }

}

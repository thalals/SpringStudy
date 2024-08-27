package com.study.concurrency.application;

import com.study.concurrency.domain.Repository.TicketCountRepository;
import com.study.concurrency.domain.Repository.TicketRepository;
import com.study.concurrency.domain.Repository.TicketRepositoryForLock;
import com.study.concurrency.domain.Repository.TicketReservationRepository;
import com.study.concurrency.domain.Ticket;
import com.study.concurrency.domain.TicketReservation;
import com.study.concurrency.infra.TicketReservationProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static java.lang.Thread.sleep;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketService {

    private final TicketReservationRepository ticketReservationRepository;
    private final TicketRepository ticketRepository;
    private final DataSource dataSource;

    private final TicketCountRepository ticketCountRepository;
    private final TicketReservationProducer ticketReservationProducer;
    private final TicketRepositoryForLock ticketRepositoryForLock;
    private final View error;

    /**
     * Thread Access Block <br>
     * 1. @Synchronized    <br>
     * 2. redis + kafka(메세징 시스템)
     */
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
        int number = ticket.getNumber();

        ticket.decrease();
        ticketRepository.save(ticket);
        ticketReservationRepository.save(TicketReservation.create(ticket, number));
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

    /**
     *  Lock 을 사용   <br>
     *  1. Pessimistic Lock (비관적)   <br>
     *  2. Optimistic Lock (낙관적)    <br>
     *  3. Named Lock             <br>
     *  4. 분산 락
     */
    @Transactional
    public void reservationToPessimisticLock(final Long ticketId) {

        final Ticket ticket = ticketRepositoryForLock.findByWithPessimisticLock(ticketId).get();
        reservationSuccess(ticket);
    }

    @Transactional
    @Retryable(
        retryFor = {ObjectOptimisticLockingFailureException.class},
        maxAttempts = 10,
        backoff = @Backoff(delay = 100)
    )
    public void reservationToOptimisticLock(final Long ticketId) throws InterruptedException {

                final Ticket ticket = ticketRepositoryForLock.findByWithOptimisticLock(ticketId).get();
                reservationSuccess(ticket);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void reservationToNamedLock(final Long ticketId) {

        final Ticket ticket = ticketRepository.findById(ticketId).get();
        reservationSuccess(ticket);

    }
}

package com.study.concurrency;

import com.study.concurrency.application.TicketService;
import com.study.concurrency.domain.Repository.TicketCountRepository;
import com.study.concurrency.domain.Repository.TicketRepositoryForLock;
import com.study.concurrency.domain.Ticket;
import com.study.concurrency.domain.Repository.TicketRepository;
import com.study.concurrency.domain.Repository.TicketReservationRepository;
import com.study.concurrency.infra.TicketReservationProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class ThreadAccessTest {

    @Autowired
    TicketService ticketService;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TicketReservationRepository ticketReservationRepository;

    @Autowired
    TicketCountRepository ticketCountRepository;

    @Autowired
    TicketReservationProducer ticketReservationProducer;

    @Autowired
    TicketRepositoryForLock ticketRepositoryForLock;

    @BeforeEach
    public void clear() {
        ticketCountRepository.clear();
    }

    @Test
    @DisplayName("Redis + Kafka 제어 성공")
    void concurrencyFailTest() throws InterruptedException {

        //give
        Ticket ticket = ticketRepository.save(Ticket.create(1L, 100));

        //when
        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                    try {
                        ticketService.reservationToPessimisticLock(1L);
                    } finally {
                        latch.countDown();
                    }
                }
            );
        }

        latch.await();

        Thread.sleep(10000);
        Ticket result = ticketRepository.findById(1L).get();

        //then
        assertAll(
            () -> assertThat(result.getStock()).isZero(),
            () -> assertThat(ticketReservationRepository.findAll().size()).isEqualTo(100)
        );
    }
}

package com.study.concurrency;

import com.study.concurrency.application.TicketService;
import com.study.concurrency.domain.Ticket;
import com.study.concurrency.domain.TicketRepository;
import com.study.concurrency.domain.TicketReservationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class ThreadAccessTest {

    @Autowired
    TicketService ticketService;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TicketReservationRepository ticketReservationRepository;


    @Test
    @DisplayName("동시다발적으로 요청이 들어올때 실패함")
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
                        ticketService.reservationWithoutTransactional(1L);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } finally {
                        latch.countDown();
                    }
                }
            );
        }

        latch.await();

        Ticket result = ticketRepository.findById(1L).get();

        //then
        assertAll(
            () -> assertThat(result.getStock()).isZero(),
            () -> assertThat(ticketReservationRepository.findAll().size()).isEqualTo(100)
        );
    }
}

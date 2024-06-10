package com.study.concurrency;

import com.study.concurrency.domain.Ticket;
import com.study.concurrency.domain.TicketRepository;
import com.study.concurrency.domain.TicketReservation;
import com.study.concurrency.domain.TicketReservationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class InitTest {

    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    TicketReservationRepository ticketReservationRepository;

    @Test
    @DisplayName("티켓 생성과 티켓 예약 생성 테스트")

    void test() {

        LocalDateTime performanceDate = LocalDateTime.of(2024, Month.AUGUST, 30, 20, 30);
        int quantity = 10;
        String name = "액션가면 대왕 공연";

        Ticket ticket = ticketRepository.save(Ticket.create(name, quantity, performanceDate));
        ticketReservationRepository.save(TicketReservation.create(ticket, ticket.getNumber()));

        //then
        assertThat(ticketRepository.findAll().size()).isEqualTo(1);
        assertThat(ticketReservationRepository.findAll().size()).isEqualTo(1);
    }
}

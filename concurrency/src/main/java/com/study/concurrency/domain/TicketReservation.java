package com.study.concurrency.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Slf4j
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class TicketReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column
    private Integer number;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    public TicketReservation(Ticket ticket, Integer number) {
        this.ticket = ticket;
        this.number = number;
    }

    public static TicketReservation create(Ticket ticket, int number) {
        log.info("예약 생성 : " + number);
        return new TicketReservation(ticket, number);
    }

}

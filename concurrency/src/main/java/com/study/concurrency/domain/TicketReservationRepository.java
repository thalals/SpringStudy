package com.study.concurrency.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketReservationRepository extends JpaRepository<TicketReservation, Long> {

}

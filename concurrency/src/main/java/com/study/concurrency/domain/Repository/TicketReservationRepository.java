package com.study.concurrency.domain.Repository;

import com.study.concurrency.domain.TicketReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketReservationRepository extends JpaRepository<TicketReservation, Long> {

}

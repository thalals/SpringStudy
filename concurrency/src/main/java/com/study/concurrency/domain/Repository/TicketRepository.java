package com.study.concurrency.domain.Repository;

import com.study.concurrency.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}

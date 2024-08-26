package com.study.concurrency.domain.Repository;

import com.study.concurrency.domain.Ticket;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface TicketRepositoryForLock extends JpaRepository<Ticket, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    @Override
    Optional<Ticket> findById(Long aLong);
}
